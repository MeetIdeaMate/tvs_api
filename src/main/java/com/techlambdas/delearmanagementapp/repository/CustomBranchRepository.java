package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.response.BranchResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomBranchRepository {
    List<BranchResponse> getAllBranches(String branchId, String branchName, String mobileNo, String city);

    Page<BranchResponse> getAllBranchesWithPage(String branchId, String branchName, String mobileNo, String city);
}
