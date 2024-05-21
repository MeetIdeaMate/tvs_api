package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "vendors")
public class Vendor {
    @Id
    private String vendorId;
    private String vendorName;
    private String mobileNo;
    private String accountNo;
    private String accountHolderName;
    private String bankName;
    private String branch;
    private String ifscCode;
    private String city;
    private String emailId;
    private String panNumber;
    private String gstNumber;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}