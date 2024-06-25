package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseItem {
    private double unitRate;
    private double value;
    private double discount;
    private double taxableValue;
    private List<GstDetail> gstDetails;
    private List<Taxes> taxes;
    private List<Incentive>incentives;
    private double invoiceValue;
    private double finalInvoiceValue;
}
