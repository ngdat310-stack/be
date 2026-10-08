package com.dinechain.service.impl;

import com.dinechain.constant.StockMovementType;
import com.dinechain.dto.request.UpdateInventoryRequest;
import com.dinechain.dto.response.InventoryResponse;
import com.dinechain.entity.BranchInventoryEntity;
import com.dinechain.entity.OrderItemEntity;
import com.dinechain.entity.RecipeEntity;
import com.dinechain.entity.StockMovementEntity;
import com.dinechain.exception.AppException;
import com.dinechain.exception.ErrorCode;
import com.dinechain.repository.BranchInventoryRepository;
import com.dinechain.repository.RecipeRepository;
import com.dinechain.repository.StockMovementRepository;
import com.dinechain.security.BranchAccessGuard;
import com.dinechain.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final BranchInventoryRepository inventoryRepository;
    private final RecipeRepository recipeRepository;
    private final StockMovementRepository stockMovementRepository;
    private final BranchAccessGuard branchAccessGuard;

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventory(Integer staffId, Integer branchId, Integer ingredientId) {
        branchAccessGuard.check(staffId, branchId);
        return InventoryResponse.from(findInventory(branchId, ingredientId));
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(Integer staffId, Integer branchId, Integer ingredientId, UpdateInventoryRequest request) {
        branchAccessGuard.check(staffId, branchId);
        BranchInventoryEntity inventory = findInventory(branchId, ingredientId);

        // Stock count correction: keep the difference in the stock history
        BigDecimal newQuantity = BigDecimal.valueOf(request.getQuantity());
        StockMovementEntity movement = new StockMovementEntity();
        movement.setBranchId(branchId);
        movement.setIngredientId(ingredientId);
        movement.setType(StockMovementType.ADJUSTMENT);
        movement.setQuantity(newQuantity.subtract(inventory.getQuantity()));
        movement.setNote(request.getNotes());
        movement.setCreatedBy(staffId);
        stockMovementRepository.save(movement);

        inventory.setQuantity(newQuantity);
        BranchInventoryEntity updated = inventoryRepository.save(inventory);
        log.info("Updated inventory for branch: {}, ingredient: {}", branchId, ingredientId);
        return InventoryResponse.from(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getInventoriesByBranch(Integer staffId, Integer branchId) {
        branchAccessGuard.check(staffId, branchId);
        return inventoryRepository.findByBranchId(branchId)
            .stream()
            .map(InventoryResponse::from)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getLowStockItems(Integer staffId, Integer branchId) {
        branchAccessGuard.check(staffId, branchId);
        return inventoryRepository.findLowStock(branchId)
            .stream()
            .map(InventoryResponse::from)
            .toList();
    }

    // Called when a cook starts a dish: ingredients leave the store according to the recipe.
    // Stock may go below zero (the dish is already being cooked); low-stock reports surface that.
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deductForDish(Integer branchId, OrderItemEntity item, Integer staffId) {
        LocalDateTime now = LocalDateTime.now();
        for (RecipeEntity recipe : recipeRepository.findByMenuItemId(item.getMenuItemId())) {
            BigDecimal amount = recipe.getQuantity().multiply(BigDecimal.valueOf(item.getQuantity()));
            if (inventoryRepository.deduct(branchId, recipe.getIngredientId(), amount, now) == 0) {
                log.warn("Branch {} does not stock ingredient {} used by {}", branchId, recipe.getIngredientId(), item.getItemName());
                continue;
            }
            StockMovementEntity movement = new StockMovementEntity();
            movement.setBranchId(branchId);
            movement.setIngredientId(recipe.getIngredientId());
            movement.setType(StockMovementType.EXPORT_KITCHEN);
            movement.setQuantity(amount.negate());
            movement.setOrderId(item.getOrderId());
            movement.setNote(item.getQuantity() + " x " + item.getItemName());
            movement.setCreatedBy(staffId);
            stockMovementRepository.save(movement);
        }
    }

    private BranchInventoryEntity findInventory(Integer branchId, Integer ingredientId) {
        return inventoryRepository.findByBranchIdAndIngredientId(branchId, ingredientId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                "Ingredient " + ingredientId + " is not stocked at branch " + branchId));
    }
}
