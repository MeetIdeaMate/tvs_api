package com.techlambdas.delearmanagementapp.exception;


public class AlreadyExistException extends RuntimeException{

    public AlreadyExistException(){
        super();
    }

    public AlreadyExistException(final String message){
        super(message);
    }

    public AlreadyExistException(final String message, final Throwable cause){
        super(message,cause);
    }


}
