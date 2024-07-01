package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String userId;
    private String userName;
    private String designation;
    private String useRefId;
    private boolean isPasswordReset;
    private String branchId;
    private String branchName;
    private boolean isMainBranch;
}
