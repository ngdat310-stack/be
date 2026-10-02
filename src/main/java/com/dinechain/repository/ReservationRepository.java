package com.dinechain.repository;

import com.dinechain.constant.ReservationStatus;
import com.dinechain.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    List<ReservationEntity> findByBranchIdAndStatus(Integer branchId, ReservationStatus status);
    List<ReservationEntity> findByReservedAtBetween(LocalDateTime start, LocalDateTime end);
    List<ReservationEntity> findByCustomerId(Integer customerId);
}
