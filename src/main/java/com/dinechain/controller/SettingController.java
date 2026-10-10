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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Tag(name = "Setting", description = "Cài đặt chung của chuỗi")
public class SettingController {

    private final SettingService settingService;

    public record VatSetting(@NotNull @DecimalMin("0") @DecimalMax("100") BigDecimal percent) {
    }

    // Empty or null url = no transfer QR: web takeaway guests can then only pay cash at the counter
    public record TransferQrSetting(
        @jakarta.validation.constraints.Pattern(regexp = "^$|^https?://\\S+$|^/api/v1/public/images/\\S+$", message = "Link must start with http:// or https://")
        @jakarta.validation.constraints.Size(max = 1000) String url) {
    }

    @GetMapping("/transfer-qr")
    @PreAuthorize(Roles.ADMIN)
    @Operation(summary = "Link ảnh mã QR chuyển khoản", description = "Khách đặt mang về chọn chuyển khoản sẽ thấy ảnh này")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TransferQrSetting> getTransferQr() {
        return ResponseEntity.ok(new TransferQrSetting(settingService.getTransferQrUrl()));
    }

    @PutMapping("/transfer-qr")
    @PreAuthorize(Roles.ADMIN)
    @Operation(summary = "Đổi link ảnh mã QR chuyển khoản", description = "{\"url\": \"https://...png\"}; để trống = tắt chuyển khoản")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TransferQrSetting> updateTransferQr(@CurrentUser Integer staffId, @Valid @RequestBody TransferQrSetting request) {
        return ResponseEntity.ok(new TransferQrSetting(settingService.updateTransferQrUrl(staffId, request.url())));
    }

    @PutMapping(value = "/transfer-qr/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(Roles.ADMIN)
    @Operation(summary = "Tải ảnh mã QR chuyển khoản lên", description = "Form-data field \"file\": ảnh PNG, JPEG hoặc WebP, tối đa 2MB. Thay link QR bằng ảnh vừa tải")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TransferQrSetting> uploadTransferQr(@CurrentUser Integer staffId,
                                                              @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(new TransferQrSetting(settingService.uploadTransferQr(staffId, file.getContentType(), file.getBytes())));
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
