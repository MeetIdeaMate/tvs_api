package com.techlambdas.delearmanagementapp.model;
import com.techlambdas.delearmanagementapp.constant.ConfigType;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "config")
public class Config {
    @Id
    private String id;
    @Indexed(unique = true)
    private String configId;
    private List<String> configuration;
    private String defaultValue;
    private ConfigType configType;
    private String configController;//Input/Output
    private String inputType;//Yes/No/Input textbox/Output
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;

}