package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.model.GstDetail;
import com.techlambdas.delearmanagementapp.model.Incentive;
import com.techlambdas.delearmanagementapp.model.Taxes;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ItemDetailResponse {
    private String partNo;
    private String itemName;
    private String categoryId;
    private String categoryName;
    private String hsnSacCode;
    private List<Map<String,String>> mainSpecValues;
    private Map<String, String> specificationsValue;
    private double unitRate;
    private int quantity;
    private double value;
    private double discount;
    private double taxableValue;
    private List<GstDetail> gstDetails;
    private List<Taxes> taxes;
    private List<Incentive> incentives;
    private double invoiceValue;
    private double finalInvoiceValue;
}
