package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatementFileDetailsRes {
    private String statementId;
    private String fileName;
    private LocalDate fromDate;
    private LocalDate toDate;
}
