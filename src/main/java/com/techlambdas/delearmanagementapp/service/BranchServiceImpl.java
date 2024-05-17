package com.techlambdas.delearmanagementapp.service;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.BranchMapper;
import com.techlambdas.delearmanagementapp.model.Branch;
import com.techlambdas.delearmanagementapp.repository.BranchRepository;
import com.techlambdas.delearmanagementapp.repository.CustomBranchRepository;
import com.techlambdas.delearmanagementapp.request.BranchRequest;
import com.techlambdas.delearmanagementapp.response.BranchResponse;
import com.techlambdas.delearmanagementapp.response.SubBranch;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private BranchMapper branchMapper;
    @Autowired
    private CustomBranchRepository customBranchRepository;

    @Override
    public Branch createBranch(BranchRequest branchRequest) {
        try {
        Branch branch =   branchMapper.mapBranchRequestToBranch(branchRequest);
        branch.setBranchId(RandomIdGenerator.getRandomId());
        return branchRepository.save(branch);
         } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<BranchResponse> getAllBranches(String branchId, String branchName, String mobileNo, String city) {
        List<BranchResponse>branches=customBranchRepository.getAllBranches(branchId,branchName,mobileNo,city);
        return branches;
    }

    @Override
    public Page<BranchResponse> getAllBranchesWithPage(String branchId, String branchName, String mobileNo, String city, Pageable pageable) {
        Page<BranchResponse>branches=customBranchRepository.getAllBranchesWithPage(branchId,branchName,mobileNo,city);
       return branches;
    }

    @Override
    public BranchResponse getBranchByBranchId(String branchId) {
        Branch branch=branchRepository.findByBranchId(branchId);
        if (branch==null)
            throw new DataNotFoundException("Branch not found");
        BranchResponse branchResponse=mapBranchResponseWithBranch(branch);
        return branchResponse;
    }

    private BranchResponse mapBranchResponseWithBranch(Branch branch) {
        BranchResponse branchResponse=branchMapper.mapEntityWithResponse(branch);
        if (branchResponse.isMainBranch()){
            List<Branch> childBranch=branchRepository.findByMainBranchId(branch.getBranchId());
            List<SubBranch>subBranches=branchMapper.mapSubBranchWithBranches(childBranch);
            branchResponse.setSubBranches(subBranches);
        }
        return branchResponse;
    }

    @Override
    public Branch updateBranch(String branchId, BranchRequest branchRequest) {
        Branch branch=branchRepository.findByBranchId(branchId);
        if (branch==null)
            throw new DataNotFoundException("Branch not found");
        return branchRepository.save(branch);
    }

    @Override
    public void deleteByBranchId(String branchId) {
        Branch branch=branchRepository.findByBranchId(branchId);
        if (branch==null)
            throw new DataNotFoundException("Branch not found");
        branchRepository.delete(branch);
    }
}
