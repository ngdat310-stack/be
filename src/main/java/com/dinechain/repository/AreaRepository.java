package com.dinechain.repository;

import com.dinechain.entity.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity, Integer> {
}
