package com.dinechain.service;

import com.dinechain.dto.request.StockImportRequest;
import com.dinechain.dto.response.StockImportResponse;
import java.util.Collection;
import java.util.Set;

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
    // true when the branch holds every ingredient of the dish's recipe for that many portions (no recipe = true)
    boolean enoughFor(Integer branchId, Integer menuItemId, int portions);
    // Dishes among these that cannot make even one portion from the branch's stock
    Set<Integer> outOfStock(Integer branchId, Collection<Integer> menuItemIds);
    // Stock taken by hand or from a spreadsheet: creates missing ingredients and sets each quantity
    StockImportResponse importStock(Integer staffId, Integer branchId, List<StockImportRequest.Row> rows);
}
