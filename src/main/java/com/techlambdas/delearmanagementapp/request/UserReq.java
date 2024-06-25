package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

@Data
public class UserReq {
    private String userName;
    private String designation;
    private String mobileNo;
    private String passWord;
    private String useRefId;
    private String branchId;
}