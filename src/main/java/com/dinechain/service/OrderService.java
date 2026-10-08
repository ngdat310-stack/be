package com.dinechain.service;

import com.dinechain.dto.request.AddOrderItemsRequest;
import com.dinechain.dto.request.CreateOrderRequest;
import com.dinechain.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(Integer staffId, CreateOrderRequest request);
    OrderResponse getOrder(Integer staffId, Integer orderId);
    OrderResponse completeOrder(Integer staffId, Integer orderId);
    OrderResponse cancelOrder(Integer staffId, Integer orderId);
    OrderResponse addItems(Integer staffId, Integer orderId, AddOrderItemsRequest request);
    OrderResponse cancelItem(Integer staffId, Integer orderId, Integer itemId, String reason);
    List<OrderResponse> getOrdersByBranch(Integer staffId, Integer branchId);
    List<OrderResponse> getOrdersByCustomer(Integer staffId, Integer customerId);
}
