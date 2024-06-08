package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.PurchaseItem;
import com.techlambdas.delearmanagementapp.model.SalesItem;
import com.techlambdas.delearmanagementapp.model.TransferDetail;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StockRequest {
    private String partNo;
    private String categoryId;
    private Map<String,String> mainSpecValue;
    private Map<String,String> specificationsValue;
    private int quantity;
    private PurchaseItem purchaseItem;
    private SalesItem salesItem;
    private String branchId;
//    private List<TransferDetail> transferDetails;
}
