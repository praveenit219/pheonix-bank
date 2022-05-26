package com.phoenix.phoenixbankapp.repository;

import com.phoenix.phoenixbankapp.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindUser() {
        User user = new User("Bob", "bob123");
        userRepository.save(user);
        User userById = userRepository.findOneByUserId("Bob");
        assertThat(userById).isNotNull();
        userById = userRepository.findOneByUserId("Tom");
        assertThat(userById).isNull();
    }
}
