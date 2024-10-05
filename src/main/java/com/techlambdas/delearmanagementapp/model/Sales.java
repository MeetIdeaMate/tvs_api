package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection="sales")
public class Sales {

    @Id
    private String id;
    private String salesId;
    private String invoiceType;  // - M-Vehile/E-Vechile/Accessories
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String billType;   //- Pay / Credit
    private String customerId;
    private String bookingNo;
    private List<PaidDetail> paidDetails;
    private List<ItemDetail> itemDetails;
    private int totalQty;
    private double totalTaxableAmt;
    private double totalDisc;
    private double totalCgst;
    private double totalSgst;
    private double totalIgst;
    private double totalIncentiveAmount;
    private double Rto;
    private double mandatoryFitting;
    private double optionFitting;
    private double others;
    private double totalInvoiceAmt;
    private double roundOffAmt;
    private double netAmt;
    private PaymentStatus paymentStatus;
    private String insuranceId;
    private loanInfo loaninfo;
    private String branchId;
    private Map<String,String> mandatoryAddons;
    private Map<String,String>  evBattery;
    private String reason;
    private boolean isCancelled;

    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}
