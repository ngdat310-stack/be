package com.dinechain.service;

import com.dinechain.dto.request.CreateReservationRequest;
import com.dinechain.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(CreateReservationRequest request);
    ReservationResponse getReservation(Integer reservationId);
    ReservationResponse confirmReservation(Integer reservationId);
    ReservationResponse cancelReservation(Integer reservationId);
    List<ReservationResponse> getReservationsByBranch(Integer branchId);
    List<ReservationResponse> getReservationsByCustomer(Integer customerId);
}
