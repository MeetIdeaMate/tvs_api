package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

import java.util.Map;

@Data
public class ItemResponse {
    private String id;
    private String itemId;
    private String partNo;
    private String itemName;
    private String categoryId;
    private String categoryName;
    private boolean isTaxable;
    private boolean isIncentive;
    private String hsnSacCode;
    private Map<String, Double> addOns;
}
