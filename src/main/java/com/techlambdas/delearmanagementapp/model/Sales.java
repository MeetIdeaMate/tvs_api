package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection="sales")
public class Sales {
    @Id
    private String id;
    private String invoiceType;  // - M-Vehile/E-Vechile/Accessories
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String billType;   //- Pay / Credit
    private String customerId;
    private List<PaidDetails> paidDetails;
    private List<ItemDetail> itemDetails;
    private int totalQty;
    private double totalTaxableAmt;
    private double totalDisc;
    private double totalCgst;
    private double totalSgst;
    private double totalInvoiceAmt;
    private double roundOffAmt;
    private double netAmt;
    private PaymentStatus paymentStatus;
    private Insurance insurance;
    private loanInfo loaninfo;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}
