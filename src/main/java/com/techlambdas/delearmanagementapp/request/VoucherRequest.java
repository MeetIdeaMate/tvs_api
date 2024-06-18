package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VoucherRequest {
    private LocalDate voucherDate;
    private String paidTo;
    private double paidAmount;
    private String Reason;
    private String approvedPay;
}
