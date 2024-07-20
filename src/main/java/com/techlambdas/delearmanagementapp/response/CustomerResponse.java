package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

@Data
public class CustomerResponse {
    private String id;
    private String customerId;
    private String customerName;
    private String mobileNo;
    private String emailId;
    private String aadharNo;
    private String accountNo;
//    private String branchId;
//    private String branchName;
    private String ifsc;
    private String city;
    private String address;
    private String image;
}
