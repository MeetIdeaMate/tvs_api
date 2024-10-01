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
    private String bookingNo;
    private List<PaidDetail> paidDetails;
    private List<ItemDetail> itemDetails;
    private int totalQty;
    private double rtoCharges;
    private double mandatoryFitting;
    private double optionFitting;
    private double others;
    private double roundOffAmt;
    private double netAmt;
    private PaymentStatus paymentStatus;
    private Map<String,String> mandatoryAddons;
    private InsuranceRequest insurance;
    private loanInfo loaninfo;
    private Map<String,String> evBattery;
}
