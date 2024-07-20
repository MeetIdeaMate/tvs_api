package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PurchaseDetail {
    private String purchaseNo;
    private String vendorId;
    private String vendorName;
    private String p_invoiceNo;
    private LocalDate p_invoiceDate;
    private String p_orderRefNo;
    private String partNumber;
    private String itemName;
    private String hsnSacCode;
    private int quantity;
    private double uniteRate;
    private double taxableValue;
    private double totalGstAmount;
    private double totalInvoiceValue;
    private double totalIncentive;
    private double finalInvoiceValue;
}
