package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SalesRequest {
    private String stockId;
    private String branchId;
    private String invoiceType;
    private LocalDate invoiceDate;
    private String billType;
    private String customerId;
    private List<PaidDetail> paidDetails;
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
}
