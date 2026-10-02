package com.dinechain.repository;

import com.dinechain.constant.BranchStatus;
import com.dinechain.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Integer> {
    Optional<BranchEntity> findByCode(String code);
    List<BranchEntity> findByStatus(BranchStatus status);
    List<BranchEntity> findByNameContainingIgnoreCase(String name);
}
