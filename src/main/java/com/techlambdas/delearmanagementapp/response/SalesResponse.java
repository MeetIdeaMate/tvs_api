package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.model.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SalesResponse {

    private String invoiceType;  // - M-Vehile/E-Vechile/Accessories
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String BillType;   //- Pay / Credit
//    private String customerId;
    private List<ItemDetail> itemDetails;
    private int totalQty;
    private double totalTaxableAmt;
    private double totalDisc;
    private double totalCgst;
    private double totalSgst;
    private double totalInvoiceAmt;
    private double roundOffAmt;
    private double netAmt;
    private List<PaidDetails> paidDetails;
    private PaymentStatus paymentStatus;
    private String customerId;
    private String customerName;
    private String mobileNo;
    private Insurance insurance;
    private loanInfo loaninfo;
    private double pendingAmt;


}
