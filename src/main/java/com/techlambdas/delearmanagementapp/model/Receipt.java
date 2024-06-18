package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "receipts")
public class Receipt {
    private String id;
    private String receiptId;
    private String receiptNo;
    private LocalDate receiptDate;
    private String customerId;
    private double receivedAmount;
    private String partNo;
    private PaymentType paymentType;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}