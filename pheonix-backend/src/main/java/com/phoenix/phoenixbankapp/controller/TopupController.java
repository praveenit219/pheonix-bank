package com.phoenix.phoenixbankapp.controller;

import com.phoenix.phoenixbankapp.domain.*;
import com.phoenix.phoenixbankapp.exception.ProcessException;
import com.phoenix.phoenixbankapp.services.TopupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

@Controller
public class TopupController {

    private static final Logger log = LoggerFactory.getLogger(TopupController.class);

    private final TopupService topupService;

    public TopupController(TopupService topupService) {
        this.topupService = topupService;
    }

    @PostMapping(value = "/topup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TopUpResponse> topupself(@RequestBody TopupRequest topupRequest) {
        log.info("top up self user - controller");
        TopUp topup = topupService.topUp(topupRequest.getAmount(), topupRequest.getUser());
        if (Objects.nonNull(topup)) {
            TopUpResponse topUpResponse = new TopUpResponse();
            topUpResponse.setBalance(topup.getAmount());
            topUpResponse.setUser(topup.getUserId());
            topUpResponse.setMessage(topupService.message(topup));
            return new ResponseEntity<>(topUpResponse, HttpStatus.OK);
        }
        throw new ProcessException("something wrong with topup process, please try later");
    }

    @PostMapping(value = "/pay", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TopUpPayResponse> topupanother(@RequestBody TopupPayRequest topupPayRequest) {
        log.info("top up another user - controller");
        TopUp senderTopup = topupService.topUpAnotherClient(topupPayRequest.getSender(), topupPayRequest.getReceiver(), topupPayRequest.getAmount(), 1);
        // BigDecimal receiverAmountTopup = topupService.calculateAmountToSend(senderTopup, topupPayRequest.getAmount());
        TopUp receiverTopup = topupService.topUpAnotherClient(topupPayRequest.getReceiver(), null, senderTopup.getTransferAmount(), 0);
        TopUpPayResponse topUpPayResponse = topupService.topUpTransaction(senderTopup, receiverTopup, senderTopup.getTransferAmount());
        if (Objects.nonNull(topUpPayResponse))
            return new ResponseEntity<>(topUpPayResponse, HttpStatus.OK);
        throw new ProcessException("something wrong with topup pay another user process, please try later");
    }

}
