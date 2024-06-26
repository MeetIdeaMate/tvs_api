package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ItemDetail {
    private String partNo;
    private String itemName;
    private String categoryId;
    private String hsnSacCode;
    private Map<String,String> mainSpecValue;
    private Map<String,String> specificationsValue;
    private double unitRate;
    private int quantity;
    private double value;
    private double discount;
    private double taxableValue;
    private List<GstDetail> gstDetails;
    private List<Taxes> taxes;
    private List<Incentive>incentives;
    private double invoiceValue;
    private double finalInvoiceValue;
    private String stockId;
}

