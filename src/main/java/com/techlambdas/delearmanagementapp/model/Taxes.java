package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

@Data
public class Taxes {
    private String taxName;
    private double percentage;
    private double taxAmount;
}
