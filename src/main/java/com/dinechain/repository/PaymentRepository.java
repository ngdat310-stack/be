package com.dinechain.repository;

import com.dinechain.constant.PaymentStatus;
import com.dinechain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    Optional<PaymentEntity> findByOrderId(Integer orderId);
    List<PaymentEntity> findByStatus(PaymentStatus status);
    boolean existsByOrderIdAndStatus(Integer orderId, PaymentStatus status);

    // Cash actually kept per branch since a point in time. Rows of [branchId, amount]
    @Query("select o.branchId, sum(p.amount) from PaymentEntity p join OrderEntity o on o.id = p.orderId "
        + "where p.status = com.dinechain.constant.PaymentStatus.PAID and p.paidAt >= :since group by o.branchId")
    List<Object[]> sumPaidSinceGroupByBranch(@Param("since") LocalDateTime since);
}
