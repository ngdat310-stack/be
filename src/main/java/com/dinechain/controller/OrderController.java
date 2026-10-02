package com.dinechain.controller;

import com.dinechain.dto.request.CreateOrderRequest;
import com.dinechain.dto.response.OrderResponse;
import com.dinechain.service.OrderService;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order management endpoints")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Tạo đơn hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Lấy đơn hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PutMapping("/{orderId}/complete")
    @Operation(summary = "Hoàn thành đơn hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponse> completeOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.completeOrder(orderId));
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "Hủy đơn hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Danh sách đơn hàng theo chi nhánh")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<OrderResponse>> getOrdersByBranch(@PathVariable Integer branchId) {
        return ResponseEntity.ok(orderService.getOrdersByBranch(branchId));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Danh sách đơn hàng theo khách hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }
}
