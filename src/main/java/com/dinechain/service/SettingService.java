package com.dinechain.service;

import java.math.BigDecimal;

public interface SettingService {
    BigDecimal getVatPercent();
    BigDecimal updateVatPercent(Integer staffId, BigDecimal percent);
}
