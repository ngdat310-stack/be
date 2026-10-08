package com.dinechain.dto.response;

import com.dinechain.constant.OrderStatus;
import com.dinechain.constant.OrderType;
import com.dinechain.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Integer id;
    private String code;
    private Integer branchId;
    private Integer tableId;
    private Integer customerId;
    private Integer staffId;
    private OrderType orderType;
    private OrderStatus status;
    private String notes;
    private BigDecimal subtotal;
    private Integer promotionId;
    private BigDecimal discountAmount;
    private Integer pointsUsed;
    private BigDecimal pointsDiscount;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> items;

    public static OrderResponse from(OrderEntity entity, List<OrderItemResponse> items) {
        return from(entity, items, null);
    }

    public static OrderResponse from(OrderEntity entity, List<OrderItemResponse> items, BigDecimal pointValue) {
        OrderResponse response = new OrderResponse();
        response.setId(entity.getId());
        response.setCode(entity.getCode());
        response.setBranchId(entity.getBranchId());
        response.setTableId(entity.getTableId());
        response.setCustomerId(entity.getCustomerId());
        response.setStaffId(entity.getOpenedBy());
        response.setOrderType(entity.getType());
        response.setStatus(entity.getStatus());
        response.setNotes(entity.getNote());
        response.setSubtotal(entity.getSubtotal());
        response.setPromotionId(entity.getPromotionId());
        response.setDiscountAmount(entity.getDiscountAmount());
        response.setPointsUsed(entity.getPointsUsed());
        if (pointValue != null && entity.getPointsUsed() != null) {
            response.setPointsDiscount(pointValue.multiply(BigDecimal.valueOf(entity.getPointsUsed())));
        }
        response.setVatAmount(entity.getVatAmount());
        response.setTotalAmount(entity.getTotalAmount());
        response.setCreatedAt(entity.getOpenedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setItems(items);
        return response;
    }
}
