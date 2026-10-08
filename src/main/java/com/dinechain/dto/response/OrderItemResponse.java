package com.dinechain.dto.response;

import com.dinechain.constant.OrderItemStatus;
import com.dinechain.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Integer id;
    private Integer orderId;
    private Integer menuItemId;
    private String itemName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal amount;
    private OrderItemStatus status;
    private String notes;
    private String cancelReason;

    public static OrderItemResponse from(OrderItemEntity entity) {
        return new OrderItemResponse(entity.getId(), entity.getOrderId(), entity.getMenuItemId(), entity.getItemName(),
            entity.getUnitPrice(), entity.getQuantity(), entity.getAmount(), entity.getStatus(), entity.getNote(),
            entity.getCancelReason());
    }
}
