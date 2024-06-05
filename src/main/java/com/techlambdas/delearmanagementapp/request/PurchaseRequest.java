package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.GstType;
import com.techlambdas.delearmanagementapp.model.ItemDetail;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseRequest {
    private String vendorId;
    private String branchId;
    private String p_invoiceNo;
    private LocalDate p_invoiceDate;
    private String p_orderRefNo;
    private List<ItemDetail> itemDetails;
    private int totalQty;
    private double totalValue;
    private double totalGstAmount;
    private double totalTaxAmount;
    private double totalIncentiveAmount;
    private double totalInvoiceAmount;
    private double finalTotalInvoiceAmount;
}
