package com.techlambdas.delearmanagementapp.mapper;
import com.techlambdas.delearmanagementapp.model.Branch;
import com.techlambdas.delearmanagementapp.request.BranchRequest;
import com.techlambdas.delearmanagementapp.response.BranchResponse;
import com.techlambdas.delearmanagementapp.response.SubBranch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    Branch mapBranchRequestToBranch(BranchRequest request);
    void updateBranchFromRequest(BranchRequest request, @MappingTarget Branch branch);

    BranchResponse mapEntityWithResponse(Branch branch);

    List<SubBranch> mapSubBranchWithBranches(List<Branch> childBranch);
}