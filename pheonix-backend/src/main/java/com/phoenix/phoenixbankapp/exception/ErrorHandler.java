package com.phoenix.phoenixbankapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody
    ExceptionResponse UnAuthorizedException(final Exception ex) {
        return handle401UnAuthorized(ex.getLocalizedMessage());
    }

    @ExceptionHandler({ProcessException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ExceptionResponse InternalProcessException(final Exception ex) {
        return handle500Exception(ex.getLocalizedMessage());
    }

    private ExceptionResponse handle401UnAuthorized(String localizedMessage) {
        return buildExceptionResponse(localizedMessage,
                HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    private ExceptionResponse handle500Exception(String localizedMessage) {
        return buildExceptionResponse(localizedMessage,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    private ExceptionResponse buildExceptionResponse(String details, String message) {
        return new ExceptionResponse(message, details);
    }

}
