package com.mcyldz.hrinventory.exception.model;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends ApplicationException {
    public BusinessLogicException(ErrorCode errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}