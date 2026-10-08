package com.dinechain.repository;

import com.dinechain.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<SettingEntity, Integer> {
    Optional<SettingEntity> findByScopeKeyAndSettingKey(Integer scopeKey, String settingKey);
}
