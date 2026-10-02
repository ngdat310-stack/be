package com.dinechain.service.impl;

import com.dinechain.constant.ReservationStatus;
import com.dinechain.dto.request.CreateReservationRequest;
import com.dinechain.dto.response.ReservationResponse;
import com.dinechain.entity.ReservationEntity;
import com.dinechain.repository.ReservationRepository;
import com.dinechain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        ReservationEntity reservation = ReservationEntity.builder()
            .branchId(request.getBranchId())
            .customerId(request.getCustomerId())
            .status(ReservationStatus.PENDING)
            .build();

        ReservationEntity saved = reservationRepository.save(reservation);
        log.info("Created reservation: {} for date: {}", saved.getId(), request.getReservationDate());
        return ReservationResponse.from(saved);
    }

    @Override
    public ReservationResponse getReservation(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return ReservationResponse.from(reservation);
    }

    @Override
    @Transactional
    public ReservationResponse confirmReservation(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        log.info("Confirmed reservation: {}", reservationId);
        return ReservationResponse.from(reservation);
    }

    @Override
    @Transactional
    public ReservationResponse cancelReservation(Integer reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        log.info("Cancelled reservation: {}", reservationId);
        return ReservationResponse.from(reservation);
    }

    @Override
    public List<ReservationResponse> getReservationsByBranch(Integer branchId) {
        return reservationRepository.findByBranchIdAndStatus(branchId, ReservationStatus.PENDING)
            .stream()
            .map(ReservationResponse::from)
            .toList();
    }

    @Override
    public List<ReservationResponse> getReservationsByCustomer(Integer customerId) {
        return reservationRepository.findByCustomerId(customerId)
            .stream()
            .map(ReservationResponse::from)
            .toList();
    }
}
