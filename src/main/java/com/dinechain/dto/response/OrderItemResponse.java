package com.dinechain.dto.response;

import com.dinechain.constant.OrderItemStatus;
import com.dinechain.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Integer id;
    private Integer orderId;
    private Integer menuItemId;
    private Integer quantity;
    private OrderItemStatus status;
    private String notes;

    public static OrderItemResponse from(OrderItemEntity entity) {
        return new OrderItemResponse(entity.getId(), entity.getOrderId(), entity.getMenuItemId(), entity.getQuantity(), entity.getStatus(), entity.getNote());
    }
}
