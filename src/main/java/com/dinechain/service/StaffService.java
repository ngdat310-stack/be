package com.dinechain.service;

import com.dinechain.dto.request.CreateStaffRequest;
import com.dinechain.dto.request.UpdateStaffRequest;
import com.dinechain.dto.response.StaffDetailResponse;

import java.util.List;

public interface StaffService {
    StaffDetailResponse createStaff(CreateStaffRequest request);
    StaffDetailResponse getStaff(Integer staffId);
    StaffDetailResponse updateStaff(Integer staffId, UpdateStaffRequest request);
    void deleteStaff(Integer staffId);
    List<StaffDetailResponse> getStaffByBranch(Integer branchId);
    List<StaffDetailResponse> searchStaff(String fullName);
}
