package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InsuranceRequest {
    private String insuranceNo;
    private String InsuranceCompanyName;
    private double insuredAmt;
    private double premiumAmt;
    private String invoiceNo;
    private LocalDate insuredDate;
    private LocalDate ownDmgExpiryDate;
    private LocalDate thirdPartyExpiryDate;
    private String customerId;
    private String vehicleNo;
}
