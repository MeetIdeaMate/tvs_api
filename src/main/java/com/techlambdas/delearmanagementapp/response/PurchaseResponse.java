package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.model.ItemDetail;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseResponse {
    private String id;
    private String purchaseNo;
    private String branchId;
    private String branchName;
    private String vendorId;
    private String vendorName;
    private String p_invoiceNo;
    private LocalDate p_invoiceDate;
    private String p_orderRefNo;
    private List<ItemDetailResponse> itemDetails;
    private int totalQty;
    private double totalValue;
    private double totalGstAmount;
    private double totalTaxAmount;
    private double totalIncentiveAmount;
    private double totalInvoiceAmount;
    private double finalTotalInvoiceAmount;
    private LocalDateTime createdDateTime;
    private String createdBy;
    private LocalDateTime updatedDateTime;
    private String updatedBy;
}
