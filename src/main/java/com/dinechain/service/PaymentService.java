package com.dinechain.service;

import com.dinechain.dto.request.CreatePaymentRequest;
import com.dinechain.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(Integer staffId, CreatePaymentRequest request);
    PaymentResponse getPayment(Integer staffId, Integer paymentId);
    PaymentResponse getPaymentByOrder(Integer staffId, Integer orderId);
    PaymentResponse refundPayment(Integer staffId, Integer paymentId);
}
