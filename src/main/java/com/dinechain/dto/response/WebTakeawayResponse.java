package com.dinechain.dto.response;

import com.dinechain.constant.OrderStatus;
import com.dinechain.constant.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

/**
 * What the guest's tracking page shows: pay first (cash at the counter or transfer to the chain's QR),
 * then follow the dishes through the kitchen until pickup.
 */
public record WebTakeawayResponse(String token, String code, String branchName, String branchAddress, String guestName,
                                  OrderStatus status, PaymentMethod payMethod, boolean paid, String transferQrUrl,
                                  BigDecimal subtotal, BigDecimal vatAmount, BigDecimal totalAmount,
                                  List<GuestOrderResponse.Item> items) {
}
