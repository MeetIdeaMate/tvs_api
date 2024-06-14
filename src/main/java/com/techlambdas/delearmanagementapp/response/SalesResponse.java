package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.model.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SalesResponse {
    private String salesId;
    private String stockId;
    private String invoiceType;  // - M-Vehile/E-Vechile/Accessories
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String billType;   //- Pay / Credit
    private String customerId;
    private String customerName;
    private String mobileNo;
    private List<PaidDetail> paidDetails;
    private List<ItemDetail> itemDetails;
    private PaymentStatus paymentStatus;
    private int totalQty;
    private double totalTaxableAmt;
    private double totalDisc;
    private double totalCgst;
    private double totalSgst;
    private double totalInvoiceAmt;
    private double roundOffAmt;
    private double netAmt;
    private Insurance insurance;
    private loanInfo loaninfo;
    private double pendingAmt;
    private String branchId;
    private String branchName;
}
