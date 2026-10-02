package com.dinechain.controller;

import com.dinechain.dto.request.CreateCustomerRequest;
import com.dinechain.dto.response.CustomerResponse;
import com.dinechain.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Tạo khách hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Lấy thông tin khách hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Cập nhật khách hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Integer customerId, @Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, request));
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "Xoá khách hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm khách hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<CustomerResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(customerService.searchCustomers(q));
    }
}
