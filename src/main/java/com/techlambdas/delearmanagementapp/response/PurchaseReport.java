package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.model.GstDetail;
import com.techlambdas.delearmanagementapp.model.Incentive;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseReport {
    private String itemName;
    private String variant;
    private String hsnSacCode;
    private int quantity;
    private double uniteRate;
    private double taxableValue;
    private List<GstDetail> gstDetail;
    private double totalInvoiceValue;
    private List<Incentive> incentives;
    private double totalIncentive;
    private double finalInvoiceValue;
    private double overallAmount;
}
