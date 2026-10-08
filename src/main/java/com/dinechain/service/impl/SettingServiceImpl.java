package com.dinechain.service.impl;

import com.dinechain.entity.SettingEntity;
import com.dinechain.repository.SettingRepository;
import com.dinechain.service.SettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Chain-wide settings kept in the database so the owner can change them without a redeploy.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    // scope_key 0 = whole chain
    private static final int CHAIN_SCOPE = 0;
    private static final String VAT_PERCENT = "VAT_PERCENT";

    private final SettingRepository settingRepository;

    // Used until the owner stores a rate (8% = reduced VAT for restaurants in force until end of 2026)
    @Value("${dinechain.vat-percent:8}")
    private BigDecimal defaultVatPercent;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getVatPercent() {
        return settingRepository.findByScopeKeyAndSettingKey(CHAIN_SCOPE, VAT_PERCENT)
            .map(setting -> new BigDecimal(setting.getSettingValue()))
            .orElse(defaultVatPercent);
    }

    @Override
    @Transactional
    public BigDecimal updateVatPercent(Integer staffId, BigDecimal percent) {
        SettingEntity setting = settingRepository.findByScopeKeyAndSettingKey(CHAIN_SCOPE, VAT_PERCENT)
            .orElseGet(() -> {
                SettingEntity created = new SettingEntity();
                created.setScopeKey(CHAIN_SCOPE);
                created.setSettingKey(VAT_PERCENT);
                created.setDescription("VAT rate applied to every order, in percent");
                return created;
            });
        setting.setSettingValue(percent.stripTrailingZeros().toPlainString());
        setting.setUpdatedBy(staffId);
        setting.setUpdatedAt(LocalDateTime.now());
        settingRepository.save(setting);
        log.info("VAT changed to {}% by staff {}", percent, staffId);
        return percent;
    }
}
