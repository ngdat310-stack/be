package com.dinechain.service.impl;

import com.dinechain.dto.request.UpdateInventoryRequest;
import com.dinechain.dto.response.InventoryResponse;
import com.dinechain.entity.BranchInventoryEntity;
import com.dinechain.repository.BranchInventoryRepository;
import com.dinechain.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final BranchInventoryRepository inventoryRepository;

    @Override
    public InventoryResponse getInventory(Integer branchId, Integer ingredientId) {
        BranchInventoryEntity inventory = inventoryRepository.findByBranchIdAndIngredientId(branchId, ingredientId)
            .orElseThrow(() -> new RuntimeException("Inventory not found"));
        return InventoryResponse.from(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(Integer branchId, Integer ingredientId, UpdateInventoryRequest request) {
        BranchInventoryEntity inventory = inventoryRepository.findByBranchIdAndIngredientId(branchId, ingredientId)
            .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (request.getQuantity() != null) {
            inventory.setQuantity(java.math.BigDecimal.valueOf(request.getQuantity()));
        }

        BranchInventoryEntity updated = inventoryRepository.save(inventory);
        log.info("Updated inventory for branch: {}, ingredient: {}", branchId, ingredientId);
        return InventoryResponse.from(updated);
    }

    @Override
    public List<InventoryResponse> getInventoriesByBranch(Integer branchId) {
        return inventoryRepository.findByBranchId(branchId)
            .stream()
            .map(InventoryResponse::from)
            .toList();
    }

    @Override
    public List<InventoryResponse> getLowStockItems(Integer branchId) {
        return inventoryRepository.findByBranchId(branchId)
            .stream()
            .map(InventoryResponse::from)
            .toList();
    }
}
