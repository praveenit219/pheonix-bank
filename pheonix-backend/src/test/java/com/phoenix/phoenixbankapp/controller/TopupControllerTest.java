package com.phoenix.phoenixbankapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.phoenixbankapp.config.TestBase;
import com.phoenix.phoenixbankapp.domain.TopUp;
import com.phoenix.phoenixbankapp.services.TopupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TopupController.class)
public class TopupControllerTest {

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
    void topupselfSuccessTest() throws Exception {
        when(this.topupService.topUp(any(BigDecimal.class), anyString())).thenReturn(testBase.prepareMockTopup());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBase.topupRequest()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("user").value("tom"))
                .andExpect(jsonPath("balance").value("123"));
    }

    @Test
    void topupselfFailureTest() throws Exception {
        when(this.topupService.topUp(any(BigDecimal.class), anyString())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBase.topupRequest()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("message").value("Internal Server Error"))
                .andExpect(jsonPath("details").value("something wrong with topup process, please try later"));
    }


    @Test
    void topupanotherFailureTest() throws Exception {
        when(this.topupService.topUpAnotherClient(anyString(), anyString(), any(BigDecimal.class), anyInt())).thenReturn(testBase.prepareMockTopup());
        when(this.topupService.topUpTransaction(any(TopUp.class), any(TopUp.class), any(BigDecimal.class))).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBase.prepareMockTopupPayRequest()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("message").value("Internal Server Error"))
                .andExpect(jsonPath("details").value("something wrong with topup pay another user process, please try later"));
    }

    //@Test
    void topupanotherSuccessTest() throws Exception {
        given(topupService.topUpTransaction(any(TopUp.class), any(TopUp.class), any(BigDecimal.class))).willReturn(testBase.prepareMockTopupResponse());
        given(topupService.topUpAnotherClient(anyString(), anyString(), any(BigDecimal.class), anyInt())).willReturn(testBase.prepareMockTopup());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBase.prepareMockTopupPayRequest()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("sender").value("tom"))
                .andExpect(jsonPath("transferAmount").value(123))
                .andExpect(jsonPath("availableBalance").value(456))
                .andExpect(jsonPath("receiver").value("bob"));
    }
}
