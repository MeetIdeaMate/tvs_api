package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.model.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class SalesDetail {
    private String salesId;
    private String invoiceType;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String billType;
    private List<PaidDetail> paidDetails;
    private List<ItemDetailResponse> itemDetails;
    private PaymentStatus paymentStatus;
    private int totalQty;
    private double totalTaxableAmt;
    private double totalDisc;
    private double totalCgst;
    private double totalSgst;
    private double totalInvoiceAmt;
    private double totalPaidAmt;
    private double totalIncentiveAmt;
    private double roundOffAmt;
    private double netAmt;
    private double pendingAmt;
    private String branchId;
    private String branchName;
}
