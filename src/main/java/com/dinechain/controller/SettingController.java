package com.dinechain.controller;

import com.dinechain.security.CurrentUser;
import com.dinechain.security.Roles;
import com.dinechain.service.SettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Tag(name = "Setting", description = "Cài đặt chung của chuỗi")
public class SettingController {

    private final SettingService settingService;

    public record VatSetting(@NotNull @DecimalMin("0") @DecimalMax("100") BigDecimal percent) {
    }

    @GetMapping("/vat")
    @Operation(summary = "Thuế VAT hiện tại (%)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<VatSetting> getVat() {
        return ResponseEntity.ok(new VatSetting(settingService.getVatPercent()));
    }

    @PutMapping("/vat")
    @PreAuthorize(Roles.ADMIN)
    @Operation(summary = "Đổi thuế VAT (%)", description = "Áp dụng cho các đơn tạo sau khi đổi; đơn cũ giữ nguyên")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<VatSetting> updateVat(@CurrentUser Integer staffId, @Valid @RequestBody VatSetting request) {
        return ResponseEntity.ok(new VatSetting(settingService.updateVatPercent(staffId, request.percent())));
    }
}
