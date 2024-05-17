package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

@Data
public class UserPwdChangReq {
    private String mobileNo;
    private String oldPassword;
    private String newPassword;
}