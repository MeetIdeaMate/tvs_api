package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "branches")
public class Branch {
    @Id
    private String id;
    private String branchId;
    private String branchName;
    private String mobileNo;
    private String pinCode;
    private String city;
    private String address;
    private boolean isMainBranch;
    private String mainBranchId;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}
