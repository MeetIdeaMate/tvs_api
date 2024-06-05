package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "purchases")
public class Purchase {
    @Id
    private String id;
    private String purchaseNo;
    private String vendorId;
    private String branchId;
    private String p_invoiceNo;
    private LocalDate p_invoiceDate;
    private String p_orderRefNo;
    private List<ItemDetail> itemDetails;
    private int totalQty;
    private double totalValue;
    private double totalGstAmount;
    private double totalTaxAmount;
    private double totalIncentiveAmount;
    private double totalInvoiceAmount;
    private double finalTotalInvoiceAmount;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}