package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

@Data
public class BranchRequest {
    private String branchName;
    private String mobileNo;
    private String pinCode;
    private String city;
    private String address;
    private boolean isMainBranch;
    private String mainBranchId;
}
