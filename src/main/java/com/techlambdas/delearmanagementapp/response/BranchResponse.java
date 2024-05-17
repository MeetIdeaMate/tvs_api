package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.model.Branch;
import lombok.Data;

import java.util.List;

@Data
public class BranchResponse {
    private String id;
    private String branchId;
    private String branchName;
    private String mobileNo;
    private String pinCode;
    private String city;
    private String address;
    private boolean isMainBranch;
    private String mainBranchId;
    private List<SubBranch> subBranches;
}
