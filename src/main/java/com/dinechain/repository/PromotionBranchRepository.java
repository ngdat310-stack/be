package com.dinechain.repository;

import com.dinechain.entity.PromotionBranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionBranchRepository extends JpaRepository<PromotionBranchEntity, Integer> {
    List<PromotionBranchEntity> findByPromotionId(Integer promotionId);
    void deleteByPromotionId(Integer promotionId);
}
