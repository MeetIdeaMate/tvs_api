package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

@Data
public class loanInfo {
    private String loanId;
    private String bankName;
    private String branchName;
    private double loanAmt;
    private boolean isCredited;
}
