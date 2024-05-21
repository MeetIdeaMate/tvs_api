package com.techlambdas.delearmanagementapp.model.submodel;

import lombok.Data;

@Data
public class AccessoriesDetail {
    private String materialNumber;
    private String materialName;
    private String hsnSacCode;
    private double unitRate;
    private int quantity;
    private double amount;
    private double discount;
    private double cgstPercentage;
    private double sgstPercentage;
    private double igstPercentage;
    private double totalAmount;
}
