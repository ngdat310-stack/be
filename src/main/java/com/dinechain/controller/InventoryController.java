package com.dinechain.controller;

import com.dinechain.dto.request.UpdateInventoryRequest;
import com.dinechain.dto.response.InventoryResponse;
import com.dinechain.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory management endpoints")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/branch/{branchId}/ingredient/{ingredientId}")
    @Operation(summary = "Lấy kho nguyên liệu")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Integer branchId, @PathVariable Integer ingredientId) {
        return ResponseEntity.ok(inventoryService.getInventory(branchId, ingredientId));
    }

    @PutMapping("/branch/{branchId}/ingredient/{ingredientId}")
    @Operation(summary = "Cập nhật kho nguyên liệu")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable Integer branchId, @PathVariable Integer ingredientId, @Valid @RequestBody UpdateInventoryRequest request) {
        return ResponseEntity.ok(inventoryService.updateInventory(branchId, ingredientId, request));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Danh sách kho theo chi nhánh")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<InventoryResponse>> getByBranch(@PathVariable Integer branchId) {
        return ResponseEntity.ok(inventoryService.getInventoriesByBranch(branchId));
    }

    @GetMapping("/branch/{branchId}/low-stock")
    @Operation(summary = "Danh sách hàng hết/thiếu")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<InventoryResponse>> getLowStock(@PathVariable Integer branchId) {
        return ResponseEntity.ok(inventoryService.getLowStockItems(branchId));
    }
}
