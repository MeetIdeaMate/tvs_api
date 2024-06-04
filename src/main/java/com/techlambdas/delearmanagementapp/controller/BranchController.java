package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Branch;
import com.techlambdas.delearmanagementapp.request.BranchRequest;
import com.techlambdas.delearmanagementapp.response.BranchResponse;
import com.techlambdas.delearmanagementapp.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/branch")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping
    public ResponseEntity<Branch> createBranch(@RequestBody BranchRequest branchRequest) {
        Branch branch = branchService.createBranch(branchRequest);
        return successResponse(HttpStatus.CREATED,"branch",branch);
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllBranches(@RequestParam(required = false) String branchId,
                                                       @RequestParam(required = false) String branchName,
                                                       @RequestParam(required = false) String mobileNo,
                                                       @RequestParam(required = false) String city) {
        List<BranchResponse> branches = branchService.getAllBranches(branchId, branchName, mobileNo, city);
        return successResponse(HttpStatus.OK,"branchResponseList",branches);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<BranchResponse>> getAllBranchesWithPage(@RequestParam(required = false) String branchId,
                                                               @RequestParam(required = false) String branchName,
                                                               @RequestParam(required = false) String mobileNo,
                                                               @RequestParam(required = false) String city,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BranchResponse> branchesPage = branchService.getAllBranchesWithPage(branchId, branchName, mobileNo, city, pageable);
        return successResponse(HttpStatus.OK,"branchWithPage",branchesPage);
    }
    @GetMapping("/{branchId}")
    public ResponseEntity<BranchResponse> getBranchById(@PathVariable String branchId) {
        BranchResponse branch = branchService.getBranchByBranchId(branchId);
        return successResponse(HttpStatus.OK,"branchResponse",branch);
    }
    @PutMapping("/{branchId}")
    public ResponseEntity<Branch> updateBranch(@PathVariable String branchId, @RequestBody BranchRequest branchRequest) {
        Branch branch = branchService.updateBranch(branchId, branchRequest);
        return successResponse(HttpStatus.OK,"branch",branch);
    }
    @DeleteMapping("/{branchId}")
    public ResponseEntity<String> deleteBranch(@PathVariable String branchId) {
        branchService.deleteByBranchId(branchId);
        return successResponse(HttpStatus.OK,"success","Deleted Successfully");
    }
}
