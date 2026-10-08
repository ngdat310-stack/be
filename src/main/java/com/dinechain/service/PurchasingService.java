package com.dinechain.service;

import com.dinechain.dto.request.CreateIngredientRequest;
import com.dinechain.dto.request.CreatePurchaseOrderRequest;
import com.dinechain.dto.request.CreateSupplierRequest;
import com.dinechain.dto.request.ReceivePurchaseOrderRequest;
import com.dinechain.dto.response.IngredientResponse;
import com.dinechain.dto.response.PurchaseOrderResponse;
import com.dinechain.dto.response.SupplierResponse;

import java.util.List;

public interface PurchasingService {
    SupplierResponse createSupplier(CreateSupplierRequest request);
    SupplierResponse updateSupplier(Integer supplierId, CreateSupplierRequest request);
    SupplierResponse deactivateSupplier(Integer supplierId);
    List<SupplierResponse> getSuppliers();

    IngredientResponse createIngredient(CreateIngredientRequest request);
    List<IngredientResponse> getIngredients();

    PurchaseOrderResponse createPurchaseOrder(Integer staffId, CreatePurchaseOrderRequest request);
    PurchaseOrderResponse getPurchaseOrder(Integer staffId, Integer purchaseOrderId);
    List<PurchaseOrderResponse> getPurchaseOrders(Integer staffId, Integer branchId);
    PurchaseOrderResponse placeOrder(Integer staffId, Integer purchaseOrderId);
    PurchaseOrderResponse receive(Integer staffId, Integer purchaseOrderId, ReceivePurchaseOrderRequest request);
    PurchaseOrderResponse cancel(Integer staffId, Integer purchaseOrderId);
}
