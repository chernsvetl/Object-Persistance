package com.github.object.persistence.exception;

public class FieldNotFoundInEntityException extends RuntimeException{
    public FieldNotFoundInEntityException(String message) {
        super(message);
    }
}
