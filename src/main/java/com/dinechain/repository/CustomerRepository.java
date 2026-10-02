package com.dinechain.repository;

import com.dinechain.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByPhone(String phone);
    Optional<CustomerEntity> findByEmail(String email);
    List<CustomerEntity> findByFullNameContainingIgnoreCase(String fullName);
}
