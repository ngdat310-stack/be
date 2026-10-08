package com.dinechain.controller;

import com.dinechain.dto.response.TableResponse;
import com.dinechain.repository.DiningTableRepository;
import com.dinechain.security.BranchAccessGuard;
import com.dinechain.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Table", description = "Floor plan of a branch")
public class TableController {

    private final DiningTableRepository diningTableRepository;
    private final BranchAccessGuard branchAccessGuard;

    @GetMapping("/branches/{branchId}/tables")
    @Operation(summary = "Sơ đồ bàn", description = "Danh sách bàn của chi nhánh và trạng thái (trống / có khách)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<TableResponse>> getTables(@CurrentUser Integer staffId, @PathVariable Integer branchId) {
        branchAccessGuard.check(staffId, branchId);
        return ResponseEntity.ok(diningTableRepository.findByBranchId(branchId).stream().map(TableResponse::from).toList());
    }
}
