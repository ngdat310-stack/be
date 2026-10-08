package com.dinechain.service.impl;

import com.dinechain.constant.OrderStatus;
import com.dinechain.constant.PaymentMethod;
import com.dinechain.constant.PaymentStatus;
import com.dinechain.dto.request.CreatePaymentRequest;
import com.dinechain.dto.response.PaymentResponse;
import com.dinechain.entity.OrderEntity;
import com.dinechain.entity.PaymentEntity;
import com.dinechain.exception.AppException;
import com.dinechain.exception.ErrorCode;
import com.dinechain.repository.OrderRepository;
import com.dinechain.repository.PaymentRepository;
import com.dinechain.security.BranchAccessGuard;
import com.dinechain.service.LoyaltyService;
import com.dinechain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Cash only: the cashier enters the money the customer handed over and the server works out the change.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final BranchAccessGuard branchAccessGuard;
    private final LoyaltyService loyaltyService;

    @Override
    @Transactional
    public PaymentResponse processPayment(Integer staffId, CreatePaymentRequest request) {
        if (request.getPaymentMethod() != null && request.getPaymentMethod() != PaymentMethod.CASH) {
            throw new AppException(ErrorCode.PAYMENT_METHOD_NOT_SUPPORTED);
        }

        // Lock the order: two cashiers paying the same bill at once are handled one after the other
        OrderEntity order = orderRepository.findByIdForUpdate(request.getOrderId())
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Order not found: " + request.getOrderId()));
        branchAccessGuard.check(staffId, order.getBranchId());

        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new AppException(ErrorCode.ALREADY_PAID, "Order " + order.getCode() + " has already been paid");
        }
        if (order.getStatus() != OrderStatus.OPEN) {
            throw new AppException(ErrorCode.INVALID_STATE, "Order " + order.getCode() + " is " + order.getStatus());
        }

        BigDecimal total = order.getTotalAmount();
        BigDecimal cash = request.getCashReceived();
        if (cash.compareTo(total) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_CASH,
                "Order total is " + total.toPlainString() + " but only " + cash.toPlainString() + " was received");
        }

        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(order.getId());
        payment.setMethod(PaymentMethod.CASH);
        payment.setAmount(total);
        payment.setCashReceived(cash);
        payment.setChangeAmount(cash.subtract(total));
        payment.setStatus(PaymentStatus.PAID);
        payment.setReceivedBy(staffId);
        PaymentEntity saved = paymentRepository.save(payment);

        log.info("Order {} paid in cash: total {}, received {}, change {}", order.getCode(), total, cash, saved.getChangeAmount());
        return PaymentResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Integer staffId, Integer paymentId) {
        PaymentEntity payment = findPayment(paymentId);
        checkAccess(staffId, payment.getOrderId());
        return PaymentResponse.from(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrder(Integer staffId, Integer orderId) {
        checkAccess(staffId, orderId);
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Payment not found for order " + orderId));
        return PaymentResponse.from(payment);
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(Integer staffId, Integer paymentId) {
        PaymentEntity payment = findPayment(paymentId);
        OrderEntity order = orderRepository.findByIdForUpdate(payment.getOrderId())
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Order not found: " + payment.getOrderId()));
        branchAccessGuard.check(staffId, order.getBranchId());

        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new AppException(ErrorCode.INVALID_STATE, "Payment " + paymentId + " is " + payment.getStatus());
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {
            loyaltyService.takeBackEarned(order);
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedBy(staffId);
        payment.setRefundedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        log.info("Refunded {} for order {} by staff {}", payment.getAmount(), order.getCode(), staffId);
        return PaymentResponse.from(payment);
    }

    private PaymentEntity findPayment(Integer paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Payment not found: " + paymentId));
    }

    private void checkAccess(Integer staffId, Integer orderId) {
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Order not found: " + orderId));
        branchAccessGuard.check(staffId, order.getBranchId());
    }
}
