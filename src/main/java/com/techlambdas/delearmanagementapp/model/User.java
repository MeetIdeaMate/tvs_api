package com.techlambdas.delearmanagementapp.model;


import com.techlambdas.delearmanagementapp.constant.UserStatus;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String userId;
    private String userName;
    private String useRefId;
    private String mobileNumber;
    private String password;
    private String designation;
    private UserStatus userStatus;
    private boolean isPasswordReset;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}