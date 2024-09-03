package com.techlambdas.delearmanagementapp.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(){
        super();
    }

    public TokenExpiredException(final String message){
        super(message);
    }

}
