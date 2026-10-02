package com.dinechain.dto.response;

import com.dinechain.constant.OrderStatus;
import com.dinechain.constant.OrderType;
import com.dinechain.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Integer id;
    private Integer branchId;
    private Integer tableId;
    private Integer customerId;
    private Integer staffId;
    private OrderType orderType;
    private OrderStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> items;

    public static OrderResponse from(OrderEntity entity, List<OrderItemResponse> items) {
        OrderResponse response = new OrderResponse();
        response.setId(entity.getId());
        response.setBranchId(entity.getBranchId());
        response.setTableId(entity.getTableId());
        response.setCustomerId(entity.getCustomerId());
        response.setStaffId(entity.getOpenedBy());
        response.setOrderType(entity.getType());
        response.setStatus(entity.getStatus());
        response.setNotes(entity.getNote());
        response.setCreatedAt(entity.getOpenedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setItems(items);
        return response;
    }
}
