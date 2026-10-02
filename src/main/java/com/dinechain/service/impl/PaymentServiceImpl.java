package com.dinechain.service.impl;

import com.dinechain.constant.PaymentStatus;
import com.dinechain.dto.request.CreatePaymentRequest;
import com.dinechain.dto.response.PaymentResponse;
import com.dinechain.entity.PaymentEntity;
import com.dinechain.repository.PaymentRepository;
import com.dinechain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(CreatePaymentRequest request) {
        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(java.math.BigDecimal.valueOf(request.getAmount()));
        payment.setMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.PAID);
        payment.setReferenceCode(request.getReferenceNumber());
        payment.setReceivedBy(1);

        PaymentEntity saved = paymentRepository.save(payment);
        log.info("Payment processed for order: {} amount: {}", request.getOrderId(), request.getAmount());
        return PaymentResponse.from(saved);
    }

    @Override
    public PaymentResponse getPayment(Integer paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        return PaymentResponse.from(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrder(Integer orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Payment not found for order"));
        return PaymentResponse.from(payment);
    }

    @Override
    @Transactional
    public void refundPayment(Integer paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
        log.info("Refunded payment: {}", paymentId);
    }
}
