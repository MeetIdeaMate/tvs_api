package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Branch;
import com.techlambdas.delearmanagementapp.response.BranchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomBranchRepository {
    List<Branch> getAllBranches(String branchId, String branchName, String mobileNo, String city);

    Page<Branch> getAllBranchesWithPage(String branchId, String branchName, String mobileNo, String city, Pageable pageable);
}
