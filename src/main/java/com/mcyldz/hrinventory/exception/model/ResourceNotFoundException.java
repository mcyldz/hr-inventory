package com.mcyldz.hrinventory.exception.model;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApplicationException{

    public ResourceNotFoundException(ErrorCode errorCode){
        super(HttpStatus.NOT_FOUND, errorCode);
    }

    public ResourceNotFoundException(ErrorCode errorCode, String message){
        super(HttpStatus.NOT_FOUND, errorCode, message);
    }
}
