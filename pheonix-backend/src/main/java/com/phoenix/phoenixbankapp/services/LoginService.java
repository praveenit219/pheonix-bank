package com.phoenix.phoenixbankapp.services;

import com.phoenix.phoenixbankapp.domain.User;
import com.phoenix.phoenixbankapp.domain.UserLoginRequest;
import com.phoenix.phoenixbankapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveOrUpdate(User user) {
        log.info("user saving to db");
        return userRepository.save(user);
    }

    public User loadUserByEmail(UserLoginRequest userLoginRequest) {
        log.info("user availability check against db");
        User userFromDb = userRepository.findOneByUserId(userLoginRequest.getUserId());
        if (Objects.isNull(userFromDb)) {
            User user = new User();
            BeanUtils.copyProperties(userLoginRequest, user);
            log.info("user details copied to entity {}", user);
            userFromDb = saveOrUpdate(user);
        }
        return userFromDb;
    }

    public boolean verifyLoginRequest(UserLoginRequest userLoginRequest, User user) {
        log.info("user authenticate against db");
        return userLoginRequest.getUserId().equals(user.getUserId()) && userLoginRequest.getPassword().equals(user.getPassword());
    }
}
