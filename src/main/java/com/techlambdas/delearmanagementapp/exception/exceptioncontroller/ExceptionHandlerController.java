package com.techlambdas.delearmanagementapp.exception.exceptioncontroller;


import com.techlambdas.delearmanagementapp.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static com.techlambdas.delearmanagementapp.response.AppResponse.errorResponse;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<AppError> handleAlreadyExistException(AlreadyExistException ex) {
        AppError appError = new AppError();
        appError.setTimestamp(LocalDateTime.now());
        appError.setStatus(HttpStatus.CONFLICT.value());
        appError.setMessage(ex.getMessage());
        return errorResponse(HttpStatus.CONFLICT,appError);
    }
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<AppError> handleDataNotFoundException(DataNotFoundException ex) {
        AppError appError = new AppError();
        appError.setTimestamp(LocalDateTime.now());
        appError.setStatus(HttpStatus.NOT_FOUND.value());
        appError.setMessage(ex.getMessage());
        return errorResponse(HttpStatus.NOT_FOUND,appError);
    }
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<AppError> handleInvalidDataException(InvalidDataException ex) {
        AppError appError = new AppError();
        appError.setTimestamp(LocalDateTime.now());
        appError.setStatus(HttpStatus.BAD_REQUEST.value());
        appError.setMessage(ex.getMessage());
        return errorResponse(HttpStatus.BAD_REQUEST,appError);
    }
    @ExceptionHandler(NoSystemAccessException.class)
    public ResponseEntity<AppError> handleNoSystemAccessException(NoSystemAccessException ex) {
        AppError appError = new AppError();
        appError.setTimestamp(LocalDateTime.now());
        appError.setStatus(HttpStatus.UNAUTHORIZED.value());
        appError.setMessage(ex.getMessage());
        return errorResponse(HttpStatus.UNAUTHORIZED,appError);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppError> handleBadCredentialsException(BadCredentialsException ex) {
        AppError appError = new AppError();
        appError.setTimestamp(LocalDateTime.now());
        appError.setStatus(HttpStatus.UNAUTHORIZED.value());
        appError.setMessage(ex.getMessage());
        return errorResponse(HttpStatus.UNAUTHORIZED,appError);
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<AppError> handleTokenExpiredException(TokenExpiredException ex) {
        AppError appError = new AppError();
        appError.setTimestamp(LocalDateTime.now());
        appError.setStatus(HttpStatus.UNAUTHORIZED.value());
        appError.setMessage(ex.getMessage() != null ? ex.getMessage() : "Token has expired.");
        return errorResponse(HttpStatus.UNAUTHORIZED, appError);
    }

}
