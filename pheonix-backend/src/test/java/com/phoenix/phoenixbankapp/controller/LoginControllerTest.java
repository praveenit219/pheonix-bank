package com.phoenix.phoenixbankapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.phoenixbankapp.config.TestBase;
import com.phoenix.phoenixbankapp.domain.User;
import com.phoenix.phoenixbankapp.domain.UserLoginRequest;
import com.phoenix.phoenixbankapp.services.LoginService;
import com.phoenix.phoenixbankapp.services.TopupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @MockBean
    LoginService loginService;

    @MockBean
    TopupService topupService;

    TestBase testBase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void ini() {
        testBase = new TestBase();
    }


    @Test
    void authenticateSuccessTest() throws Exception {
        when(this.loginService.loadUserByEmail(ArgumentMatchers.any(UserLoginRequest.class))).thenReturn(testBase.prepareMockUser());
        when(this.loginService.verifyLoginRequest(ArgumentMatchers.any(UserLoginRequest.class), any(User.class))).thenReturn(true);
        when(this.topupService.availableAmount(anyString())).thenReturn(new BigDecimal(123));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBase.userLoginRequest()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("user").value("tom"))
                .andExpect(jsonPath("balance").value(123));
    }

    @Test
    void authenticateFailureTest() throws Exception {
        when(this.loginService.loadUserByEmail(ArgumentMatchers.any(UserLoginRequest.class))).thenReturn(testBase.prepareMockUser());
        when(this.loginService.verifyLoginRequest(ArgumentMatchers.any(UserLoginRequest.class), any(User.class))).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBase.userLoginRequest()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value("Unauthorized"))
                .andExpect(jsonPath("details").value("user authentication failed"));
    }

    @Test
    void authenticateProcessExceptionTest() throws Exception {
        when(this.loginService.loadUserByEmail(ArgumentMatchers.any(UserLoginRequest.class))).thenReturn(null);
        when(this.loginService.verifyLoginRequest(ArgumentMatchers.any(UserLoginRequest.class), any(User.class))).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBase.userLoginRequest()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }
}
