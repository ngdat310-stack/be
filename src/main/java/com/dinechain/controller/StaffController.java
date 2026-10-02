package com.dinechain.controller;

import com.dinechain.dto.request.CreateStaffRequest;
import com.dinechain.dto.request.UpdateStaffRequest;
import com.dinechain.dto.response.StaffDetailResponse;
import com.dinechain.dto.response.ErrorResponse;
import com.dinechain.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
@Tag(name = "Staff", description = "Staff management endpoints")
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    @Operation(summary = "Tạo nhân viên mới")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Nhân viên đã được tạo"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<StaffDetailResponse> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.createStaff(request));
    }

    @GetMapping("/{staffId}")
    @Operation(summary = "Lấy thông tin nhân viên")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thông tin nhân viên"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy nhân viên", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<StaffDetailResponse> getStaff(@PathVariable Integer staffId) {
        return ResponseEntity.ok(staffService.getStaff(staffId));
    }

    @PutMapping("/{staffId}")
    @Operation(summary = "Cập nhật nhân viên")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<StaffDetailResponse> updateStaff(@PathVariable Integer staffId, @Valid @RequestBody UpdateStaffRequest request) {
        return ResponseEntity.ok(staffService.updateStaff(staffId, request));
    }

    @DeleteMapping("/{staffId}")
    @Operation(summary = "Xoá nhân viên")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteStaff(@PathVariable Integer staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Danh sách nhân viên theo chi nhánh")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<StaffDetailResponse>> getStaffByBranch(@PathVariable Integer branchId) {
        return ResponseEntity.ok(staffService.getStaffByBranch(branchId));
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm nhân viên")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<StaffDetailResponse>> searchStaff(@RequestParam(name = "q") String query) {
        return ResponseEntity.ok(staffService.searchStaff(query));
    }
}
