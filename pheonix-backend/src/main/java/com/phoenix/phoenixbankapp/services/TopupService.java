package com.phoenix.phoenixbankapp.services;

import com.phoenix.phoenixbankapp.domain.TopUp;
import com.phoenix.phoenixbankapp.domain.TopUpPayResponse;
import com.phoenix.phoenixbankapp.repository.TopupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TopupService {

    private static final Logger log = LoggerFactory.getLogger(TopupService.class);

    private final TopupRepository topupRepository;

    public TopupService(TopupRepository topupRepository) {
        this.topupRepository = topupRepository;
    }

    public TopUp topUp(BigDecimal topUpAmount, String userId) {
        TopUp topUpFromDb = topupRepository.findOneByUserId(userId);
        TopUp topUp;
        if (Objects.nonNull(topUpFromDb)) {
            TopUp calculatedAmount = topUp(topUpFromDb, topUpAmount, 0, userId);
            topUp = topupRepository.save(topUpFromDb);
            log.info("{} tops up {}, total available amount is {}", userId, topUpAmount, calculatedAmount.getAmount());
        } else {
            topUp = new TopUp();
            topUp.setAmount(topUpAmount);
            topUp.setDebtAmount(BigDecimal.ZERO);
            topUp.setTransferAmount(BigDecimal.ZERO);
            topUp.setUserId(userId);
            topupRepository.save(topUp);
            log.info("{} tops up {}", userId, topUpAmount);
        }
        return topUp;
    }

    private void updateDebtUserAmount(TopUp topUp, BigDecimal topUpAmount) {
        TopUp topUpFromDb = topupRepository.findOneByUserId(topUp.getDebtToUser());
        if (Objects.nonNull(topUpFromDb)) {
            TopUp updatedAmount = topUp(topUpFromDb, topUpAmount.abs(), 0, topUp.getDebtToUser());
            topUp = topupRepository.save(updatedAmount);
            log.info("{} tops up {}, total available amount is {}", topUp.getDebtToUser(), topUpAmount, updatedAmount.getAmount());
        } else {
            log.error("no debt user was found");
        }
    }

    public BigDecimal availableAmount(String userId) {
        log.info("fetching available amount for user {}", userId);
        TopUp topUpFromDb = topupRepository.findOneByUserId(userId);
        if (Objects.nonNull(topUpFromDb))
            return topUpFromDb.getAmount();
        return BigDecimal.ZERO;
    }

    public TopUp topUpAnotherClient(String user, String receiver, BigDecimal amount, int operation) {
        TopUp topUpDetails = topupRepository.findOneByUserId(user);
        if (Objects.nonNull(topUpDetails))
            return topUp(topUpDetails, amount, operation, receiver);
        TopUp topUp = new TopUp();
        topUp.setAmount(amount);
        topUp.setDebtAmount(BigDecimal.ZERO);
        topUp.setTransferAmount(BigDecimal.ZERO);
        topUp.setUserId(user);
        return topUp;
    }

    public TopUpPayResponse topUpTransaction(TopUp sender, TopUp receiver, BigDecimal amount) {
        topupRepository.save(sender);
        topupRepository.save(receiver);
        TopUpPayResponse topUpPayResponse = new TopUpPayResponse();
        topUpPayResponse.setReceiver(receiver.getUserId());
        topUpPayResponse.setSender(sender.getUserId());
        topUpPayResponse.setTransferAmount(amount);
        topUpPayResponse.setAvailableBalance(sender.getAmount());
        topUpPayResponse.setMessage(message(sender));
        return topUpPayResponse;
    }

    private TopUp topUp(TopUp topup, BigDecimal amount, int operation, String user) {
        BigDecimal topupAvailableAmount = topup.getAmount();
        BigDecimal topupCalculatedAmount = null;
        if (0 == operation) {
            log.info("compare is {}", topup.getDebtAmount().compareTo(BigDecimal.ZERO));
            if (topup.getDebtAmount().compareTo(BigDecimal.ZERO) == -1) {
                BigDecimal debtAmount = topup.getDebtAmount();
                BigDecimal userDebtClerance = calculateUserDebtClearance(topup, amount);
                BigDecimal debtAmountCal = BigDecimal.ZERO;
                if (debtAmount.abs().compareTo(amount) == 1)
                    debtAmountCal = debtAmount.add(amount);
                if (debtAmount.abs().compareTo(amount) == -1) {
                    topupAvailableAmount = debtAmount.add(amount);
                    topup.setAmount(topupAvailableAmount);
                }
                topup.setDebtAmount(debtAmountCal);
                updateDebtUserAmount(topup, userDebtClerance);
                if (debtAmountCal.compareTo(BigDecimal.ZERO) == 0)
                    topup.setDebtToUser(null);
            } else {
                topupCalculatedAmount = topupAvailableAmount.add(amount);
                topup.setAmount(topupCalculatedAmount);
            }
        }

        if (1 == operation && (topupAvailableAmount.compareTo(amount) == -1)) {
            BigDecimal debtAmount = topupAvailableAmount.subtract(amount).add(topup.getDebtAmount());
            topup.setDebtAmount(debtAmount);
            topup.setDebtToUser(user);
        }

        if (1 == operation && topupAvailableAmount.compareTo(amount) >= 0) {
            topupCalculatedAmount = topupAvailableAmount.subtract(amount);
            topup.setAmount(topupCalculatedAmount);
        }
        topup.setTransferAmount(calculateAmountToSend(topup, amount));
        return topup;
    }


    private BigDecimal calculateUserDebtClearance(TopUp topup, BigDecimal amount) {
        BigDecimal debtAmount = topup.getDebtAmount().abs();
        if (debtAmount.compareTo(amount) == 1)
            return amount;
        return topup.getDebtAmount().abs();
    }

    public BigDecimal calculateAmountToSend(TopUp senderTopup, BigDecimal incoming) {
        BigDecimal amoutToSend = BigDecimal.ZERO;
        if (senderTopup.getDebtAmount().compareTo(BigDecimal.ZERO) == 0)
            amoutToSend = incoming;
        if (senderTopup.getDebtAmount().compareTo(BigDecimal.ZERO) == -1 && senderTopup.getAmount().compareTo(incoming) == 1) {
            amoutToSend = incoming;
            senderTopup.setAmount(BigDecimal.ZERO);
        }
        if (senderTopup.getDebtAmount().compareTo(BigDecimal.ZERO) == -1 && senderTopup.getAmount().compareTo(incoming) == -1) {
            amoutToSend = senderTopup.getAmount();
            senderTopup.setAmount(BigDecimal.ZERO);
        }
        return amoutToSend;
    }

    public String message(TopUp topup) {
        AtomicReference<String> message = new AtomicReference<>("");
        if (topup.getDebtAmount().compareTo(BigDecimal.ZERO) == -1 && StringUtils.hasLength(topup.getDebtToUser())) {
            message.set("Owing " + topup.getDebtAmount().abs() + " to " + topup.getDebtToUser());
        }
        if (topup.getDebtAmount().compareTo(BigDecimal.ZERO) == 0 && !StringUtils.hasLength(topup.getDebtToUser())) {
            topupRepository.findAll().forEach(top -> {
                if (StringUtils.hasLength(top.getDebtToUser()) && top.getDebtToUser().equals(topup.getUserId()))
                    message.set("Owing " + top.getDebtAmount().abs() + " from " + top.getUserId());
            });
        }
        return message.get();
    }

    public String message(String user) {
        AtomicReference<String> message = new AtomicReference<>("");
        topupRepository.findAll().forEach(top -> {
            if (StringUtils.hasLength(top.getDebtToUser()) && top.getDebtToUser().equals(user))
                message.set("Owing " + top.getDebtAmount().abs() + " from " + top.getUserId());
            if (StringUtils.hasLength(top.getDebtToUser()) && top.getUserId().equals(user) && top.getDebtAmount().compareTo(BigDecimal.ZERO) == -1)
                message.set("Owing " + top.getDebtAmount().abs() + " to " + top.getDebtToUser());
        });
        return message.get();
    }
}
