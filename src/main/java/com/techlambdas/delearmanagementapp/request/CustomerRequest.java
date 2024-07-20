package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private String customerName;
    private String mobileNo;
    private String emailId;
    private String aadharNo;
    private String accountNo;
//    private String branchId;
    private String ifsc;
    private String city;
    private String address;
}
