package com.dinechain.controller;

import com.dinechain.dto.request.CreateReservationRequest;
import com.dinechain.dto.response.ReservationResponse;
import com.dinechain.service.ReservationService;
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
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "Reservation management endpoints")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Tạo đặt bàn")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(request));
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "Lấy đặt bàn")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.getReservation(reservationId));
    }

    @PutMapping("/{reservationId}/confirm")
    @Operation(summary = "Xác nhận đặt bàn")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.confirmReservation(reservationId));
    }

    @PutMapping("/{reservationId}/cancel")
    @Operation(summary = "Hủy đặt bàn")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Danh sách đặt bàn theo chi nhánh")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ReservationResponse>> getByBranch(@PathVariable Integer branchId) {
        return ResponseEntity.ok(reservationService.getReservationsByBranch(branchId));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Danh sách đặt bàn theo khách hàng")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ReservationResponse>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(reservationService.getReservationsByCustomer(customerId));
    }
}
