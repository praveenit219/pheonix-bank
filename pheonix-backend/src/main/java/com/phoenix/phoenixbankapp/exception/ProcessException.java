package com.phoenix.phoenixbankapp.exception;

public class ProcessException extends RuntimeException {

    public ProcessException(String s) {
        super(s);
    }

    public ProcessException(String s, Exception e) {
        super(s, e);
    }
}
