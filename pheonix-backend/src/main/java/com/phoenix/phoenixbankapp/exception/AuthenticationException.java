package com.phoenix.phoenixbankapp.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String s) {
        super(s);
    }

    public AuthenticationException(String s, Exception e) {
        super(s, e);
    }

}
