package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountRequest {
    private String financialYear;
    private String accountHeadCode;
    private String accountHeadName;
    private String transactorId;
    private String transactorName;
    private String transactDesc;
    private String shortNotes;
    private String transactRefNo;
    private String transactDetails;
    private LocalDate transactDate;
    private double amount;
}
