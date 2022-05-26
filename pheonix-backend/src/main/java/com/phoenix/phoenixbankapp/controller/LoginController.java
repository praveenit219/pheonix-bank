package com.phoenix.phoenixbankapp.controller;

import com.phoenix.phoenixbankapp.domain.User;
import com.phoenix.phoenixbankapp.domain.UserLoginRequest;
import com.phoenix.phoenixbankapp.domain.UserLoginResponse;
import com.phoenix.phoenixbankapp.exception.AuthenticationException;
import com.phoenix.phoenixbankapp.exception.ProcessException;
import com.phoenix.phoenixbankapp.services.LoginService;
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
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;
    private final TopupService topupService;

    public LoginController(LoginService loginService, TopupService topupService) {
        this.loginService = loginService;
        this.topupService = topupService;
    }


    @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponse> authenticate(@RequestBody UserLoginRequest userLoginRequest) {
        log.info("user authentication controller");
        User userDetails = loginService.loadUserByEmail(userLoginRequest);
        UserLoginResponse userLoginResponse;
        if (Objects.nonNull(userDetails)) {
            if (loginService.verifyLoginRequest(userLoginRequest, userDetails)) {
                userLoginResponse = new UserLoginResponse(userDetails.getUserId());
                log.info("user authentication succeed");
                userLoginResponse.setBalance(topupService.availableAmount(userDetails.getUserId()));
                userLoginResponse.setMessage(topupService.message(userDetails.getUserId()));
                return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
            } else {
                log.error("user authentication failed");
                throw new AuthenticationException("user authentication failed");
            }
        } else
            throw new ProcessException("authentication process have problem");
    }


}
