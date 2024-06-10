package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

@Data
public class loanInfo {
    // loan id ,bank name , loan amt , invoiceno
    private String loanId;
    private String bankName;
    private double loanAmt;
//    private String invoiceNo;
}
