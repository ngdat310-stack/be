package com.dinechain.service;

import com.dinechain.dto.request.UpdateInventoryRequest;
import com.dinechain.dto.response.InventoryResponse;
import com.dinechain.entity.OrderItemEntity;

import java.util.List;

public interface InventoryService {
    InventoryResponse getInventory(Integer staffId, Integer branchId, Integer ingredientId);
    InventoryResponse updateInventory(Integer staffId, Integer branchId, Integer ingredientId, UpdateInventoryRequest request);
    List<InventoryResponse> getInventoriesByBranch(Integer staffId, Integer branchId);
    List<InventoryResponse> getLowStockItems(Integer staffId, Integer branchId);
    void deductForDish(Integer branchId, OrderItemEntity item, Integer staffId);
}
