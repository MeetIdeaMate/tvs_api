package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.util.Map;

@Data
public class TransferItemReq {
    private String partNo;
    private String categoryId;
    private Map<String,String> mainSpecValue;
    private int quantity;
}