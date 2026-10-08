package com.dinechain.service;

import com.dinechain.dto.response.BranchMenuItemResponse;

import java.math.BigDecimal;
import java.util.List;

public interface BranchMenuService {
    List<BranchMenuItemResponse> getMenu(Integer staffId, Integer branchId);
    BranchMenuItemResponse setAvailability(Integer staffId, Integer branchId, Integer menuItemId, boolean available);
    BranchMenuItemResponse setPrice(Integer staffId, Integer branchId, Integer menuItemId, BigDecimal price);
}
