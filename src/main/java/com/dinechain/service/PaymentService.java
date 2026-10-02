package com.dinechain.service;

import com.dinechain.dto.request.CreatePaymentRequest;
import com.dinechain.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(CreatePaymentRequest request);
    PaymentResponse getPayment(Integer paymentId);
    PaymentResponse getPaymentByOrder(Integer orderId);
    void refundPayment(Integer paymentId);
}
