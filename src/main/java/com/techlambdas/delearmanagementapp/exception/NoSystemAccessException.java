package com.techlambdas.delearmanagementapp.exception;

public class NoSystemAccessException extends RuntimeException {
    public NoSystemAccessException(String message) {
        super(message);
    }
}