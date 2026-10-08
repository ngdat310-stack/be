package com.dinechain.repository;

import com.dinechain.entity.StockMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovementEntity, Integer> {
    List<StockMovementEntity> findByOrderId(Integer orderId);
}
