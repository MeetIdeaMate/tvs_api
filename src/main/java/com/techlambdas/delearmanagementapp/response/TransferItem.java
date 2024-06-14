package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TransferItem {
    private String partNo;
    private String itemName;
    private String categoryId;
    private String categoryName;
    private Map<String,String> mainSpecValue;
    private int quantity;
}
