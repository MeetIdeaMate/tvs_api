package com.techlambdas.delearmanagementapp.exception;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(){
        super();
    }

    public DataNotFoundException(final String message){
        super(message);
    }
}