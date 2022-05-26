package com.phoenix.phoenixbankapp.services;

import com.phoenix.phoenixbankapp.config.TestBase;
import com.phoenix.phoenixbankapp.domain.TopUp;
import com.phoenix.phoenixbankapp.domain.TopUpPayResponse;
import com.phoenix.phoenixbankapp.repository.TopupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TopUpServiceTest {

    TopupService topupService;

    @MockBean
    private TopupRepository topupRepository;


    private TestBase testBase;
    private TopUp topup;
    private TopUp recieverTopup;

    @BeforeEach
    public void ini() {
        topupService = new TopupService(topupRepository);
        testBase = new TestBase();
        topup = testBase.prepareMockTopup();
        recieverTopup = testBase.prepareMockTopup();
    }

    @Test
    void topUpUpdateExisting() {
        when(this.topupRepository.findOneByUserId(anyString())).thenReturn(topup);
        when(this.topupRepository.save(any(TopUp.class))).thenReturn(topup);
        TopUp topUp1 = topupService.topUp(BigDecimal.valueOf(234), "user1");
        Assertions.assertEquals(BigDecimal.valueOf(357), topUp1.getAmount());
        this.topup.setAmount(BigDecimal.valueOf(123));
        when(this.topupRepository.findOneByUserId(anyString())).thenReturn(this.topup);
        topUp1 = topupService.topUp(BigDecimal.valueOf(234.234), "user1");
        Assertions.assertEquals(BigDecimal.valueOf(357.234), topUp1.getAmount());
    }

    @Test
    void topUpNew() {
        when(this.topupRepository.findOneByUserId(anyString())).thenReturn(null);
        when(this.topupRepository.save(any(TopUp.class))).thenReturn(topup);
        TopUp topUp1 = topupService.topUp(BigDecimal.valueOf(234), "user1");
        Assertions.assertEquals(BigDecimal.valueOf(234), topUp1.getAmount());
    }


    @Test
    void availableAmountTest() {
        //when(this.topupRepository.findOneByUserId(anyString())).thenReturn(null);
        //Assertions.assertThrows(ProcessException.class, () -> topupService.availableAmount("tom"));
        when(this.topupRepository.findOneByUserId(anyString())).thenReturn(topup);
        BigDecimal topup = topupService.availableAmount("tom");
        Assertions.assertEquals(BigDecimal.valueOf(123), topup);
    }

    @Test
    void topUpAnotherClientTest() {
        when(this.topupRepository.findOneByUserId(anyString())).thenReturn(topup);
        TopUp topUpF = topupService.topUpAnotherClient("tom", "bob", new BigDecimal(123), 0);
        Assertions.assertNotNull(topUpF);
        Assertions.assertEquals(BigDecimal.valueOf(246), topUpF.getAmount());
        topup.setAmount(new BigDecimal(123));
        topUpF = topupService.topUpAnotherClient("tom", "bob", new BigDecimal(123), 1);
        Assertions.assertNotNull(topUpF);
        Assertions.assertEquals(BigDecimal.valueOf(0), topUpF.getAmount());
        topup.setAmount(new BigDecimal(123));
        topUpF = topupService.topUpAnotherClient("tom", "bob", BigDecimal.valueOf(234.234), 0);
        Assertions.assertNotNull(topUpF);
        Assertions.assertEquals(BigDecimal.valueOf(357.234), topUpF.getAmount());
        topUpF = topupService.topUpAnotherClient("tom", "bob", BigDecimal.valueOf(123), 1);
        Assertions.assertNotNull(topUpF);
        Assertions.assertEquals(BigDecimal.valueOf(234.234), topUpF.getAmount());
    }

    @Test
    void topUpTransactionTest() {
        when(this.topupRepository.save(any(TopUp.class))).thenReturn(topup);
        recieverTopup.setUserId("bob");
        recieverTopup.setAmount(BigDecimal.valueOf(20));
        TopUpPayResponse topUpPayResponse = topupService.topUpTransaction(topup, recieverTopup, BigDecimal.valueOf(111));
        Assertions.assertNotNull(topUpPayResponse);
        Assertions.assertEquals("tom", topUpPayResponse.getSender());
        Assertions.assertEquals("bob", topUpPayResponse.getReceiver());
        Assertions.assertEquals(BigDecimal.valueOf(123), topUpPayResponse.getAvailableBalance());
        Assertions.assertEquals(BigDecimal.valueOf(111), topUpPayResponse.getTransferAmount());
    }

    @Test
    void messageTopupTest() {
        topup.setDebtToUser("alice");
        topup.setDebtAmount(new BigDecimal(-20));
        String mess = topupService.message(topup);
        Assertions.assertNotNull(mess);
        Assertions.assertEquals("Owing 20 to alice", mess);

        topup.setDebtToUser(null);
        topup.setDebtAmount(new BigDecimal(0));
        TopUp topup1 = new TopUp();
        topup1.setUserId("bob");
        topup1.setDebtToUser("tom");
        topup1.setDebtAmount(new BigDecimal(-30));
        when(this.topupRepository.findAll()).thenReturn(List.of(topup1, topup));
        mess = topupService.message(topup);
        Assertions.assertNotNull(mess);
        Assertions.assertEquals("Owing 30 from bob", mess);
    }


    @Test
    void messageTest() {
        TopUp topup1 = new TopUp();
        topup1.setUserId("bob");
        topup1.setDebtToUser("tom");
        topup1.setDebtAmount(new BigDecimal(-30));
        when(this.topupRepository.findAll()).thenReturn(List.of(topup1, topup));
        String mess = topupService.message("tom");
        Assertions.assertNotNull(mess);
        Assertions.assertEquals("Owing 30 from bob", mess);
        mess = topupService.message("bob");
        Assertions.assertNotNull(mess);
        Assertions.assertEquals("Owing 30 to tom", mess);
    }
}
