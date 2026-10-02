package com.dinechain.dto.request;

import com.dinechain.constant.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "Branch ID is required")
    private Integer branchId;

    @NotNull(message = "Order type is required")
    private OrderType orderType;

    private Integer tableId;
    private Integer customerId;
    private Integer staffId;
    private String notes;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull(message = "Menu item ID is required")
        private Integer menuItemId;

        @NotNull(message = "Quantity is required")
        private Integer quantity;

        private Double unitPrice;
        private String notes;
    }
}
