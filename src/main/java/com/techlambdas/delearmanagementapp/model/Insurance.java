package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Insurance {
    // inc id , inc company name, insured amt ,invoiceno ,insured date,expiry date
    private String insuranceId;
    private String InsuranceCompanyName;
    private double insuredAmt;
    private String invoiceNo;
    private LocalDateTime insuredDate;
    private LocalDateTime expiryDate;
}
