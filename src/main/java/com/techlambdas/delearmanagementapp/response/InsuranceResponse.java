package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InsuranceResponse {
    private String insuranceId;
    private String insuranceNo;
    private String InsuranceCompanyName;
    private double insuredAmt;
    private double premiumAmt;
    private String invoiceNo;
    private LocalDate insuredDate;
    private LocalDate ownDmgExpiryDate;
    private LocalDate thirdPartyExpiryDate;
    private String CustomerId;
    private String customerName;
    private String MobileNo;
    private String vehicleNo;
}
