package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "items")
public class Item {
    @Id
    private String id;
    private String itemId;
    private String partNo;
    private String itemName;
    private String categoryId;
    private boolean isTaxable;
    private boolean isIncentive;
    private String hsnSacCode;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}