package com.phoenix.phoenixbankapp.services;


import com.phoenix.phoenixbankapp.config.TestBase;
import com.phoenix.phoenixbankapp.domain.TopUp;
import com.phoenix.phoenixbankapp.domain.TopUpPayResponse;
import com.phoenix.phoenixbankapp.repository.TopupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TopUpServiceDebtsTest {

    TopupService topupService;

    @Autowired
    private TopupRepository topupRepository;


    private TestBase testBase;
    private TopUp topUpAlice;
    private TopUp topUpBob;

    @BeforeEach
    public void ini() {
        topupService = new TopupService(topupRepository);
        testBase = new TestBase();
        topUpAlice = new TopUp();
        topUpAlice = topupService.topUp(new BigDecimal(100), "Alice");
        topUpBob = topupService.topUp(new BigDecimal(80), "Bob");
    }

    @AfterEach
    public void fin() {
        topupRepository.deleteAll();
    }

    @Test
    void topUpDebtsTest() {
        TopUp bobSender = topupService.topUpAnotherClient("Bob", "Alice", new BigDecimal(50), 1);
        TopUp aliceReceiver = topupService.topUpAnotherClient("Alice", null, new BigDecimal(50), 0);
        TopUpPayResponse topUpPayResponse = topupService.topUpTransaction(bobSender, aliceReceiver, new BigDecimal(50));
        System.out.println(bobSender);
        System.out.println(aliceReceiver);
        Assertions.assertEquals(new BigDecimal(30), topUpPayResponse.getAvailableBalance());
        Assertions.assertEquals(new BigDecimal(30), bobSender.getAmount());
        Assertions.assertEquals(new BigDecimal(150), aliceReceiver.getAmount());
        Assertions.assertEquals(new BigDecimal(0), bobSender.getDebtAmount());

        bobSender = topupService.topUpAnotherClient("Bob", "Alice", new BigDecimal(100), 1);
        aliceReceiver = topupService.topUpAnotherClient("Alice", null, new BigDecimal(30), 0);
        topUpPayResponse = topupService.topUpTransaction(bobSender, aliceReceiver, new BigDecimal(30));
        System.out.println(bobSender);
        System.out.println(aliceReceiver);
        Assertions.assertEquals(new BigDecimal(0), topUpPayResponse.getAvailableBalance());
        Assertions.assertEquals(new BigDecimal(0), bobSender.getAmount());
        Assertions.assertEquals(new BigDecimal(180), aliceReceiver.getAmount());
        Assertions.assertEquals(new BigDecimal(-70), bobSender.getDebtAmount());
        Assertions.assertEquals("Alice", bobSender.getDebtToUser());

        bobSender = topupService.topUp(new BigDecimal(30), "Bob");
        System.out.println(bobSender);
        System.out.println(aliceReceiver);
        Assertions.assertEquals(new BigDecimal(0), bobSender.getAmount());
        Assertions.assertEquals(new BigDecimal(-40), bobSender.getDebtAmount());
        Assertions.assertEquals("Alice", bobSender.getDebtToUser());

        TopUp aliceSender = topupService.topUpAnotherClient("Alice", "Bob", new BigDecimal(30), 1);
        TopUp bobReceiver = topupService.topUpAnotherClient("Bob", null, new BigDecimal(30), 0);
        topUpPayResponse = topupService.topUpTransaction(aliceSender, bobReceiver, new BigDecimal(30));
        System.out.println(bobSender);
        System.out.println(aliceReceiver);
        Assertions.assertEquals(new BigDecimal(0), bobSender.getAmount());
        Assertions.assertEquals(new BigDecimal(210), aliceReceiver.getAmount());
        Assertions.assertEquals(new BigDecimal(-10), bobSender.getDebtAmount());

        bobSender = topupService.topUpAnotherClient("Bob", "Alice", new BigDecimal(100), 1);
        aliceReceiver = topupService.topUpAnotherClient("Alice", null, new BigDecimal(10), 0);
        topUpPayResponse = topupService.topUpTransaction(bobSender, aliceReceiver, new BigDecimal(10));
        System.out.println(bobSender);
        System.out.println(aliceReceiver);
        Assertions.assertEquals(new BigDecimal(220), aliceReceiver.getAmount());
    }
}
