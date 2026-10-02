package com.dinechain.repository;

import com.dinechain.constant.OrderStatus;
import com.dinechain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findByBranchIdAndStatus(Integer branchId, OrderStatus status);
    List<OrderEntity> findByBranchIdAndOpenedAtBetween(Integer branchId, LocalDateTime start, LocalDateTime end);
    List<OrderEntity> findByCustomerId(Integer customerId);
}
