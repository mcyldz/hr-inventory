package com.mcyldz.hrinventory.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException{

    private final HttpStatus status;
    private final ErrorCode errorCode;

    protected ApplicationException(HttpStatus status, ErrorCode errorCode, String message){
        super(message != null ? message : errorCode.getDefaultMessage());
        this.status = status;
        this.errorCode = errorCode;
    }

    protected ApplicationException(HttpStatus status, ErrorCode errorCode){
        this(status, errorCode, null);
    }
}
