package com.dinechain.repository;

import com.dinechain.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {
    Optional<MenuItemEntity> findByCode(String code);
    List<MenuItemEntity> findByCategoryId(Integer categoryId);
    List<MenuItemEntity> findByNameContainingIgnoreCase(String name);
}
