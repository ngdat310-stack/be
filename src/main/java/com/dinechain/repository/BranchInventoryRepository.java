package com.dinechain.repository;

import com.dinechain.entity.BranchInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchInventoryRepository extends JpaRepository<BranchInventoryEntity, Integer> {
    Optional<BranchInventoryEntity> findByBranchIdAndIngredientId(Integer branchId, Integer ingredientId);
    List<BranchInventoryEntity> findByBranchId(Integer branchId);
}
