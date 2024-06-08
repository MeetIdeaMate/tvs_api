package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SalesRequest {
    private String invoiceType;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String BillType;
//    private String customerId;
    private List<PaidDetails> paidDetails;
    private List<ItemDetail> itemDetails;
    private PaymentStatus paymentStatus;
    private String customerId;
    private String customerName;
    private String mobileNo;
    private int totalQty;
    private double totalTaxableAmt;
    private double totalDisc;
    private double totalCgst;
    private double totalSgst;
    private double totalInvoiceAmt;
    private double roundOffAmt;
    private double netAmt;
}
