package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "category")
public class Category {
    @Id
    private String id;
    private String categoryId;
    private String categoryName;
    private Map<Integer,String> mainSpec;
    private Map<Integer,String> specification;
    private Map<String,Integer> taxes;
    private Map<String,Integer> incentive;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}