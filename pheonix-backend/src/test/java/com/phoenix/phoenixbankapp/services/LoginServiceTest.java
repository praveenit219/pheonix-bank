package com.phoenix.phoenixbankapp.services;

import com.phoenix.phoenixbankapp.config.TestBase;
import com.phoenix.phoenixbankapp.domain.User;
import com.phoenix.phoenixbankapp.domain.UserLoginRequest;
import com.phoenix.phoenixbankapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class LoginServiceTest {

    LoginService loginService;
    UserLoginRequest userLoginRequest;
    User user;

    @MockBean
    private UserRepository userRepository;
    private TestBase testBase;

    @BeforeEach
    public void ini() {
        loginService = new LoginService(userRepository);
        testBase = new TestBase();
        userLoginRequest = testBase.userLoginRequest();
        user = testBase.prepareMockUser();
    }

    @Test
    void saveOrUpdateTest() {
        when(this.userRepository.save(any(User.class))).thenReturn(user);
        User userFromDb = loginService.saveOrUpdate(user);
        Assertions.assertNotNull(userFromDb);
    }

    @Test
    void loadUserByEmailTest() {
        when(this.userRepository.findOneByUserId(anyString())).thenReturn(user);
        User userTest = loginService.loadUserByEmail(userLoginRequest);
        Assertions.assertNotNull(userTest);
        Assertions.assertEquals("tom", userTest.getUserId());
    }

    @Test
    void verifyLoginRequestTest() {
        Assertions.assertTrue(loginService.verifyLoginRequest(userLoginRequest, user));
        user.setPassword("sdfsd");
        Assertions.assertFalse(loginService.verifyLoginRequest(userLoginRequest, user));
    }
}
