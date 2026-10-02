package com.dinechain.repository;

import com.dinechain.constant.PaymentStatus;
import com.dinechain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    Optional<PaymentEntity> findByOrderId(Integer orderId);
    List<PaymentEntity> findByStatus(PaymentStatus status);
}
