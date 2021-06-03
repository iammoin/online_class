package com.onlineclass.exception;

public class ServiceException extends Exception{
    private final Integer errorCode;

    public ServiceException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
