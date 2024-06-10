package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.PurchaseItem;
import com.techlambdas.delearmanagementapp.model.SalesItem;
import com.techlambdas.delearmanagementapp.constant.StockStatus;
import lombok.Data;

import java.util.Map;

@Data
public class StockRequest {
    private String partNo;
    private String categoryId;
    private Map<String,String> mainSpecValue;
    private Map<String,String> specificationsValue;
    private int quantity;
    private int purchaseQuantity;
    private int salesQuantity;
    private PurchaseItem purchaseItem;
    private SalesItem salesItem;
    private String branchId;
    private StockStatus stockStatus;
    private String purchaseId;
    private String salesId;
//    private List<TransferDetail> transferDetails;
}
