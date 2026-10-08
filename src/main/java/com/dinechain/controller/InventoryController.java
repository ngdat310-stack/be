package com.dinechain.controller;

import com.dinechain.dto.request.UpdateInventoryRequest;
import com.dinechain.dto.response.InventoryResponse;
import com.dinechain.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.dinechain.security.CurrentUser;
import com.dinechain.security.Roles;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(Roles.STOCK_READ)
    public ResponseEntity<InventoryResponse> getInventory(@CurrentUser Integer staffId, @PathVariable Integer branchId, @PathVariable Integer ingredientId) {
        return ResponseEntity.ok(inventoryService.getInventory(staffId, branchId, ingredientId));
    }

    @PutMapping("/branch/{branchId}/ingredient/{ingredientId}")
    @Operation(summary = "Cập nhật kho nguyên liệu")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(Roles.STOCK)
    public ResponseEntity<InventoryResponse> updateInventory(@CurrentUser Integer staffId, @PathVariable Integer branchId, @PathVariable Integer ingredientId, @Valid @RequestBody UpdateInventoryRequest request) {
        return ResponseEntity.ok(inventoryService.updateInventory(staffId, branchId, ingredientId, request));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Danh sách kho theo chi nhánh")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(Roles.STOCK_READ)
    public ResponseEntity<List<InventoryResponse>> getByBranch(@CurrentUser Integer staffId, @PathVariable Integer branchId) {
        return ResponseEntity.ok(inventoryService.getInventoriesByBranch(staffId, branchId));
    }

    @GetMapping("/branch/{branchId}/low-stock")
    @Operation(summary = "Danh sách hàng hết/thiếu")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize(Roles.STOCK_READ)
    public ResponseEntity<List<InventoryResponse>> getLowStock(@CurrentUser Integer staffId, @PathVariable Integer branchId) {
        return ResponseEntity.ok(inventoryService.getLowStockItems(staffId, branchId));
    }
}
