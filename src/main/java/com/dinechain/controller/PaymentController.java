package com.dinechain.controller;

import com.dinechain.dto.request.CreatePaymentRequest;
import com.dinechain.dto.response.PaymentResponse;
import com.dinechain.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment management endpoints")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Xử lý thanh toán")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(request));
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Lấy thông tin thanh toán")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Lấy thanh toán theo đơn hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrder(orderId));
    }

    @PutMapping("/{paymentId}/refund")
    @Operation(summary = "Hoàn tiền")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> refundPayment(@PathVariable Integer paymentId) {
        paymentService.refundPayment(paymentId);
        return ResponseEntity.noContent().build();
    }
}
