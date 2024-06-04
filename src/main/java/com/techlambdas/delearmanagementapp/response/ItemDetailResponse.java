package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ItemDetailResponse {
    private String partNo;
    private String categoryId;
    private String categoryName;
    private Map<String, String> mainSpecValue;
    private Map<String, String> specificationsValue;
    private double unitRate;
    private int quantity;
    private double value;
    private double discount;
    private double taxableValue;
    private List<GstDetailResponse> gstDetails;
    private List<TaxesResponse> taxes;
    private List<IncentiveResponse> incentives;
    private double invoiceValue;
    private double finalInvoiceValue;
    private String hsnSacCode;
    private String itemName;
}
