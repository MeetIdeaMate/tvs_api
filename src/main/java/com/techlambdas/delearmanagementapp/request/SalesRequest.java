package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class SalesRequest {
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
    private Map<String,String> mandatoryAddons;
    private Insurance insurance;
    private loanInfo loaninfo;
    private EvBattery evBattery;
}
