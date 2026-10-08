package com.dinechain.service;

import com.dinechain.dto.request.CreatePromotionRequest;
import com.dinechain.dto.response.PromotionCheckResponse;
import com.dinechain.dto.response.PromotionResponse;
import com.dinechain.entity.PromotionEntity;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionService {
    PromotionResponse createPromotion(CreatePromotionRequest request);
    PromotionResponse updatePromotion(Integer promotionId, CreatePromotionRequest request);
    PromotionResponse deactivatePromotion(Integer promotionId);
    List<PromotionResponse> getPromotions();
    PromotionCheckResponse check(Integer staffId, String code, Integer branchId, BigDecimal orderAmount);

    /** Validates the code for this order, takes one use of it and returns the promotion; discount via {@link #discountFor}. */
    PromotionEntity redeem(String code, Integer branchId, BigDecimal orderAmount);
    BigDecimal discountFor(PromotionEntity promotion, BigDecimal orderAmount);
    void release(Integer promotionId);
    PromotionEntity get(Integer promotionId);
}
