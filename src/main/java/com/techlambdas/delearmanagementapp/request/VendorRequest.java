package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

@Data
public class VendorRequest {
    private String vendorName;
    private String mobileNo;
    private String gstNumber;
    private String city;
    private String address;
    private String accountNo;
    private String ifscCode;
    private String emailId;
}
