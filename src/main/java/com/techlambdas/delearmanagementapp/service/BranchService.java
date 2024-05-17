package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Branch;
import com.techlambdas.delearmanagementapp.request.BranchRequest;
import com.techlambdas.delearmanagementapp.response.BranchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BranchService {
    Branch createBranch(BranchRequest branchRequest);
    List<BranchResponse> getAllBranches(String branchId, String branchName, String mobileNo, String city);
    Page<BranchResponse> getAllBranchesWithPage(String branchId, String branchName, String mobileNo, String city, Pageable pageable);
    BranchResponse getBranchByBranchId(String branchId);
    Branch updateBranch(String branchId, BranchRequest branchRequest);
    void deleteByBranchId(String branchId);
}
