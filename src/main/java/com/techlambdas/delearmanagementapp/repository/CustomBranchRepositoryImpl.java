package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.response.BranchResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomBranchRepositoryImpl implements CustomBranchRepository {
    @Override
    public List<BranchResponse> getAllBranches(String branchId, String branchName, String mobileNo, String city) {
        return null;
    }

    @Override
    public Page<BranchResponse> getAllBranchesWithPage(String branchId, String branchName, String mobileNo, String city) {
        return null;
    }
}
