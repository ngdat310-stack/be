package com.dinechain.service;

import com.dinechain.dto.request.UpdateInventoryRequest;
import com.dinechain.dto.response.InventoryResponse;

import java.util.List;

public interface InventoryService {
    InventoryResponse getInventory(Integer branchId, Integer ingredientId);
    InventoryResponse updateInventory(Integer branchId, Integer ingredientId, UpdateInventoryRequest request);
    List<InventoryResponse> getInventoriesByBranch(Integer branchId);
    List<InventoryResponse> getLowStockItems(Integer branchId);
}
