package com.techlambdas.delearmanagementapp.model.submodel;

import lombok.Data;

@Data
public class AccessoriesDetail {
    private String partNumber;
    private String vehicleName;
    private String variant;
    private String color;
    private String hsnSacCode;
    private double unitRate;
    private int quantity;
    private double amount;
    private double discount;
    private double cgstPercentage;
    private double sgstPercentage;
    private double igstPercentage;
    private double tcsValue;
    private double invValue;
    private double incentive;
    private double stateIncentive;
    private double totalAmount;
}
