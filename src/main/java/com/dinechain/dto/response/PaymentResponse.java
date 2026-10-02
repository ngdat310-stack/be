package com.dinechain.dto.response;

import com.dinechain.constant.PaymentMethod;
import com.dinechain.constant.PaymentStatus;
import com.dinechain.entity.PaymentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Integer id;
    private Integer orderId;
    private java.math.BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime paidAt;

    public static PaymentResponse from(PaymentEntity entity) {
        return new PaymentResponse(entity.getId(), entity.getOrderId(), entity.getAmount(), entity.getMethod(), entity.getStatus(), entity.getPaidAt());
    }
}
