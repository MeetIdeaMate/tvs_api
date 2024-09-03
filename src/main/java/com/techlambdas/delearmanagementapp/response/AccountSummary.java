package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

@Data
public class AccountSummary {

    private String accountHeadCode;
    private String accountHeadName;
    private int receiptCount;
    private double amountCollected;
}