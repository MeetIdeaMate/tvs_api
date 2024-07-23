package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "access_controls")
public class AccessControl {
    @Id
    private String id;
    private String departmentId;
    private String userId;
    private String role;
    private List<Menu>menus;
    private List<UiComponent>uiComponents;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}
