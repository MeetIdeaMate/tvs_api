package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "accountheads")
public class AccountHead {
    @Id
    private String id;
    @Indexed(unique = true)
    private String accountHeadCode;
    private String accountHeadName;
    private AccountType accountType;
    private PricingFormat pricingFormat;
    private double minAmount;
    private double maxAmount;
    private boolean cashierOps;
    private String transferFrom;
    private String transferTo;
    private boolean needPrinting;
    private String printingTemplate;
    private String ptVariables;
    private boolean activeStatus;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
    private String lastApprovedBy;
}