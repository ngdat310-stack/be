package com.dinechain.service;

import com.dinechain.dto.request.AddOrderItemsRequest;
import com.dinechain.dto.response.GuestOrderResponse;
import com.dinechain.dto.response.GuestTableResponse;
import com.dinechain.dto.response.PublicMenuResponse;

import java.util.Optional;

public interface GuestTableService {
    PublicMenuResponse getChainMenu();
    GuestTableResponse getTable(String qrToken);
    Optional<GuestOrderResponse> getOpenOrder(String qrToken);
    GuestOrderResponse placeOrder(String qrToken, AddOrderItemsRequest request);
}
