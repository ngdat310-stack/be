package com.dinechain.service;

import com.dinechain.dto.request.CreateBranchRequest;
import com.dinechain.dto.request.UpdateBranchRequest;
import com.dinechain.dto.response.BranchResponse;

import java.util.List;

public interface BranchService {
    BranchResponse createBranch(CreateBranchRequest request);
    BranchResponse getBranch(Integer branchId);
    BranchResponse updateBranch(Integer staffId, Integer branchId, UpdateBranchRequest request);
    void deleteBranch(Integer branchId);
    List<BranchResponse> getAllBranches();
    List<BranchResponse> searchBranches(String name);
}
