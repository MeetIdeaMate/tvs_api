package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "stock")
public class Stock {
    private String id;
    private String partNo;
    private String categoryId;
    private Map<String,String> mainSpecValue;
    private Map<String,String> specificationsValue;
    private int quantity;
    private PurchaseItem purchaseItem;
    private SalesItem salesItem;
    private String branchId;
//    private List<TransferDetail> transferDetails;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}