package com.phoenix.phoenixbankapp.repository;

import com.phoenix.phoenixbankapp.domain.TopUp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TopupRepositoryTest {

    @Autowired
    private TopupRepository topupRepository;

    @Test
    void saveAndFindUserForTopup() {
        TopUp topup = new TopUp();
        topup.setUserId("user1");
        topup.setAmount(new BigDecimal("2342.234"));
        TopUp topupFromDB = topupRepository.save(topup);
        assertThat(topupFromDB).isNotNull();
        topupFromDB = topupRepository.findOneByUserId("user1");
        assertThat(topupFromDB).isNotNull();
        topupFromDB = topupRepository.findOneByUserId("tom");
        assertThat(topupFromDB).isNull();
    }
}
