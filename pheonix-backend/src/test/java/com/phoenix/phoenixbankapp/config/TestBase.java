package com.phoenix.phoenixbankapp.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.phoenixbankapp.domain.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Configuration
@ActiveProfiles("test")
public class TestBase {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static DefaultResourceLoader resource = new DefaultResourceLoader();

    public static String resourceToString(String classpath) {
        try (Reader reader = new InputStreamReader(resource.getResource(classpath).getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <T> T convertJsonStringToPojo(String content, Class<T> valueType) {
        String contents = resourceToString(content);
        try {
            return objectMapper.readValue(contents, valueType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User prepareMockUser() {
        return convertJsonStringToPojo("classpath:user.json", User.class);
    }

    public UserLoginRequest userLoginRequest() {
        return convertJsonStringToPojo("classpath:userLoginRequest.json", UserLoginRequest.class);
    }

    public TopUp prepareMockTopup() {
        return convertJsonStringToPojo("classpath:topup.json", TopUp.class);
    }

    public TopupRequest topupRequest() {
        return convertJsonStringToPojo("classpath:topupRequest.json", TopupRequest.class);
    }

    public TopUpPayResponse prepareMockTopupResponse() {
        return convertJsonStringToPojo("classpath:topupResponse.json", TopUpPayResponse.class);
    }

    public TopupPayRequest prepareMockTopupPayRequest() {
        return convertJsonStringToPojo("classpath:topupPayRequest.json", TopupPayRequest.class);
    }
}
