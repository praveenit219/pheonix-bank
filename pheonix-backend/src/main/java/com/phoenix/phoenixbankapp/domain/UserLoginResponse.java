package com.phoenix.phoenixbankapp.domain;

import java.math.BigDecimal;

public class UserLoginResponse {

    private String user;
    private BigDecimal balance;
    private String message;

    public UserLoginResponse(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


}
