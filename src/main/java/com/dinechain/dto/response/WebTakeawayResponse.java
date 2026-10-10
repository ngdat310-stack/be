package com.dinechain.dto.response;

import com.dinechain.constant.OrderStatus;
import com.dinechain.constant.PaymentMethod;
import com.dinechain.service.SettingService.BankAccount;

import java.math.BigDecimal;
import java.util.List;

/**
 * What the guest's tracking page shows: pay first (cash at the counter or transfer to the chain's QR),
 * then follow the dishes through the kitchen until pickup.
 * With a bank account set, transferQrUrl is a QR made for this order (amount and code filled in) and bankAccount says where
 * to transfer by hand; autoConfirm means the bank tells the server when the money arrives.
 */
public record WebTakeawayResponse(String token, String code, String branchName, String branchAddress, String guestName,
                                  OrderStatus status, PaymentMethod payMethod, boolean paid, String transferQrUrl,
                                  BigDecimal subtotal, BigDecimal vatAmount, BigDecimal totalAmount,
                                  List<GuestOrderResponse.Item> items, boolean transferClaimed, String cancelReason,
                                  BankAccount bankAccount, boolean autoConfirm) {
}
