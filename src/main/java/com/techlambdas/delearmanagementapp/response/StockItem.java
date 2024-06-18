package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.model.PurchaseItem;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StockItem {
    private String stockId;
    private Map<String,String> mainSpecValue;
    private int quantity;
}
