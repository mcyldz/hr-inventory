package com.mcyldz.hrinventory.exception.model;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ApplicationException{

    public DuplicateResourceException(ErrorCode errorCode){
        super(HttpStatus.CONFLICT, errorCode);
    }

    public DuplicateResourceException(ErrorCode errorCode, String message){
        super(HttpStatus.CONFLICT, errorCode, message);
    }
}
