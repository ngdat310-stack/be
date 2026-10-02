package com.dinechain.service.impl;

import com.dinechain.constant.OrderItemStatus;
import com.dinechain.constant.OrderStatus;
import com.dinechain.dto.request.CreateOrderRequest;
import com.dinechain.dto.response.OrderItemResponse;
import com.dinechain.dto.response.OrderResponse;
import com.dinechain.entity.OrderEntity;
import com.dinechain.entity.OrderItemEntity;
import com.dinechain.repository.OrderItemRepository;
import com.dinechain.repository.OrderRepository;
import com.dinechain.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        OrderEntity order = new OrderEntity();
        order.setBranchId(request.getBranchId());
        order.setType(request.getOrderType());
        order.setTableId(request.getTableId());
        order.setCustomerId(request.getCustomerId());
        order.setStatus(OrderStatus.OPEN);
        order.setOpenedBy(request.getStaffId() != null ? request.getStaffId() : 1);
        order.setSubtotal(java.math.BigDecimal.ZERO);
        order.setDiscountAmount(java.math.BigDecimal.ZERO);
        order.setTotalAmount(java.math.BigDecimal.ZERO);
        order.setNote(request.getNotes());

        OrderEntity savedOrder = orderRepository.save(order);

        java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;
        for (CreateOrderRequest.OrderItemRequest itemReq : request.getItems()) {
            java.math.BigDecimal unitPrice = itemReq.getUnitPrice() != null ?
                java.math.BigDecimal.valueOf(itemReq.getUnitPrice()) : java.math.BigDecimal.ZERO;
            java.math.BigDecimal amount = unitPrice.multiply(java.math.BigDecimal.valueOf(itemReq.getQuantity()));

            OrderItemEntity item = new OrderItemEntity();
            item.setOrderId(savedOrder.getId());
            item.setMenuItemId(itemReq.getMenuItemId());
            item.setItemName("Item");
            item.setUnitPrice(unitPrice);
            item.setQuantity(itemReq.getQuantity());
            item.setAmount(amount);
            item.setStatus(OrderItemStatus.PENDING);
            item.setNote(itemReq.getNotes());
            orderItemRepository.save(item);
            totalAmount = totalAmount.add(amount);
        }

        savedOrder.setSubtotal(totalAmount);
        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);

        log.info("Created order: {} for branch: {}", savedOrder.getId(), request.getBranchId());
        return getOrder(savedOrder.getId());
    }

    @Override
    public OrderResponse getOrder(Integer orderId) {
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        List<OrderItemResponse> items = orderItemRepository.findByOrderId(orderId)
            .stream()
            .map(OrderItemResponse::from)
            .collect(Collectors.toList());

        return OrderResponse.from(order, items);
    }

    @Override
    @Transactional
    public OrderResponse completeOrder(Integer orderId) {
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(OrderStatus.COMPLETED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        log.info("Completed order: {}", orderId);
        return getOrder(orderId);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Integer orderId) {
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        log.info("Cancelled order: {}", orderId);
        return getOrder(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersByBranch(Integer branchId) {
        return orderRepository.findByBranchIdAndStatus(branchId, OrderStatus.OPEN)
            .stream()
            .map(order -> getOrder(order.getId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByCustomer(Integer customerId) {
        return orderRepository.findByCustomerId(customerId)
            .stream()
            .map(order -> getOrder(order.getId()))
            .collect(Collectors.toList());
    }
}
