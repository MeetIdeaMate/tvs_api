package com.techlambdas.delearmanagementapp.model;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StatementConfig {
    @Id
    private String id;
    @Indexed(unique = true)
    private String statementConfigId;
    private List<String> statementConfiguration;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}
