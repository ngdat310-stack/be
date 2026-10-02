package com.dinechain.service;

import com.dinechain.dto.request.CreateOrderRequest;
import com.dinechain.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse getOrder(Integer orderId);
    OrderResponse completeOrder(Integer orderId);
    OrderResponse cancelOrder(Integer orderId);
    List<OrderResponse> getOrdersByBranch(Integer branchId);
    List<OrderResponse> getOrdersByCustomer(Integer customerId);
}
