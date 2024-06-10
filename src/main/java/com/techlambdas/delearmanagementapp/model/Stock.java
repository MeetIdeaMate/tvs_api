package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "stock")
public class Stock {
    private String id;
    private String stockId;
    private String partNo;
    private String categoryId;
    private Map<String,String> mainSpecValue;
    private Map<String,String> specificationsValue;
    private int quantity;
    private int purchaseQuantity;
    private int salesQuantity;
    private String purchaseId;
    private String salesId;
    private PurchaseItem purchaseItem;
    private SalesItem salesItem;
    private String branchId;
    private StockStatus stockStatus;
    private List<TransferDetail> transferDetails;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}
