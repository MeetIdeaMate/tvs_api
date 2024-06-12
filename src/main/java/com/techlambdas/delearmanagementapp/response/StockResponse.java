package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.constant.StockStatus;
import lombok.Data;

import java.util.Map;

@Data
public class StockResponse {
    private String stockId;
    private String partNo;
    private String itemName;
    private String categoryId;
    private String categoryName;
    private Map<String,String> mainSpecValue;
    private Map<String,String> specificationsValue;
    private int quantity;
    private String branchId;
    private String branchName;
    private String hsnSacCode;
    private StockStatus stockStatus;
}
