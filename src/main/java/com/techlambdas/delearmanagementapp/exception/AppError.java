package com.techlambdas.delearmanagementapp.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppError {
    private LocalDateTime timestamp;
    private int status;
    private String message;

}
