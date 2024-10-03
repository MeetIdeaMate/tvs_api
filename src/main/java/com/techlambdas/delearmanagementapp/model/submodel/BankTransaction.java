package com.techlambdas.delearmanagementapp.model.submodel;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BankTransaction {
    private LocalDate date;
    private String description;
    private double debit;
    private double credit;
    private double balance;
    private String PaymentType;
}