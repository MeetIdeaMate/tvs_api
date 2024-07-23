package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    @Indexed(unique = true)
    private String transacId;
    private String financialYear;
    private String accountHeadCode;
    private String accountHeadName;
    private String transactDesc;
    private String shortNotes;
    private String transactRefNo;
    private String transactDetails;
    private LocalDate transactDate;
    private AccountType transactType;
    private String transactor;
    private String transactorId;
    private String transactorType;
    private double amount;
    private boolean onRecPayOnly;
    private boolean cancelled;
    private boolean edited;
    private boolean printed;
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
