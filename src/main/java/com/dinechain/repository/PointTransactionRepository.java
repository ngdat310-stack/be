package com.dinechain.repository;

import com.dinechain.entity.PointTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransactionEntity, Integer> {
    List<PointTransactionEntity> findByCustomerIdOrderByCreatedAtDescIdDesc(Integer customerId);
    List<PointTransactionEntity> findByOrderId(Integer orderId);
}
