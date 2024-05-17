package com.techlambdas.delearmanagementapp.exception;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(){
        super();
    }

    public InvalidDataException(final String message){
        super(message);
    }
}
