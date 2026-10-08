package com.dinechain.dto.request;

import com.dinechain.constant.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {
    @NotNull(message = "Order ID is required")
    private Integer orderId;

    // Only CASH is accepted for now (no bank / e-wallet integration); defaults to CASH
    private PaymentMethod paymentMethod = PaymentMethod.CASH;

    // Money the customer handed over; change is calculated by the server
    @NotNull(message = "Cash received is required")
    @DecimalMin(value = "0", message = "Cash received must not be negative")
    private BigDecimal cashReceived;
}
