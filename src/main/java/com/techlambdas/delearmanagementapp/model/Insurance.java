package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
@Document(collection = "insurance")
@Data
public class Insurance {
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
    private String vehicleNo;
}

