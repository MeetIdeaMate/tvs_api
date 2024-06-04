package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

@Data
public class TaxesResponse {
    private String taxName;
    private double percentage;
    private double taxAmount;
}
