package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.constant.UserStatus;
import lombok.Data;

@Data
public class UserResponse{
    private String id;
    private String userId;
    private String userName;
    private String designation;
    private String useRefId;
    private boolean isPasswordReset;
    private String branchId;
    private String branchName;
    private String mobileNumber;
    private String password;
    private UserStatus userStatus;
}
