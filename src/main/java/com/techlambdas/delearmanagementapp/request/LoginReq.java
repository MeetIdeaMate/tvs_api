package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

@Data
public class LoginReq {
    private String mobileNo;
    private String passWord;
}