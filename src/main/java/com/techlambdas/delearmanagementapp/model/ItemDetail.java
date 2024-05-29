package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ItemDetail {
    private String itemId;
    private String partNo;
    private String categoryId;
    private Map<String,String> primarySpecification;
    private Map<String,String> specifications;
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
}

