package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "vendors")
public class Vendor {
    @Id
    private String id;
    private String vendorId;
    private String vendorName;
    private String mobileNo;
    private String accountNo;
    private String ifscCode;
    private String city;
    private String emailId;
    private String gstNumber;
    private String address;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}