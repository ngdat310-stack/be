package com.dinechain.service.impl;

import com.dinechain.constant.OrderStatus;
import com.dinechain.dto.request.AddOrderItemsRequest;
import com.dinechain.dto.response.GuestOrderResponse;
import com.dinechain.dto.response.GuestTableResponse;
import com.dinechain.dto.response.PublicMenuResponse;
import com.dinechain.entity.AreaEntity;
import com.dinechain.entity.BranchEntity;
import com.dinechain.entity.DiningTableEntity;
import com.dinechain.exception.AppException;
import com.dinechain.exception.ErrorCode;
import com.dinechain.repository.AreaRepository;
import com.dinechain.repository.BranchRepository;
import com.dinechain.repository.DiningTableRepository;
import com.dinechain.repository.MenuCategoryRepository;
import com.dinechain.repository.MenuItemRepository;
import com.dinechain.repository.OrderItemRepository;
import com.dinechain.repository.OrderRepository;
import com.dinechain.dto.response.OrderItemResponse;
import com.dinechain.dto.response.OrderResponse;
import com.dinechain.service.BranchMenuService;
import com.dinechain.service.GuestTableService;
import com.dinechain.service.OrderService;
import com.dinechain.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Self-ordering from the QR code printed on each table. The token in the QR is the only key a guest holds:
 * it tells which branch and table they sit at, and nothing else is reachable with it.
 */
@Service
@RequiredArgsConstructor
public class GuestTableServiceImpl implements GuestTableService {

    private final DiningTableRepository diningTableRepository;
    private final AreaRepository areaRepository;
    private final BranchRepository branchRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BranchMenuService branchMenuService;
    private final OrderService orderService;
    private final SettingService settingService;

    @Override
    @Transactional(readOnly = true)
    public PublicMenuResponse getChainMenu() {
        return new PublicMenuResponse(categories(),
            menuItemRepository.findByIsActiveTrueAndDeletedAtIsNullOrderByCategoryIdAscNameAsc().stream()
                .map(m -> new PublicMenuResponse.Dish(m.getId(), m.getCategoryId(), m.getName(), m.getDescription(),
                    m.getImageUrl(), m.getUnit(), m.getBasePrice()))
                .toList(),
            settingService.getVatPercent());
    }

    @Override
    @Transactional(readOnly = true)
    public GuestTableResponse getTable(String qrToken) {
        DiningTableEntity table = findTable(qrToken);
        AreaEntity area = areaRepository.findById(table.getAreaId()).orElseThrow();
        BranchEntity branch = branchRepository.findById(area.getBranchId())
            .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));
        return new GuestTableResponse(branch.getCode(), branch.getName(), branch.getAddress(), area.getName(),
            table.getName(), table.getCapacity(), settingService.getVatPercent(),
            categories(),
            branchMenuService.getPublicMenu(branch.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuestOrderResponse> getOpenOrder(String qrToken) {
        DiningTableEntity table = findTable(qrToken);
        return orderRepository.findFirstByTableIdAndStatusOrderByIdDesc(table.getId(), OrderStatus.OPEN)
            .map(order -> OrderResponse.from(order,
                orderItemRepository.findByOrderId(order.getId()).stream().map(OrderItemResponse::from).toList()))
            .map(this::forGuest);
    }

    @Override
    public GuestOrderResponse placeOrder(String qrToken, AddOrderItemsRequest request) {
        return forGuest(orderService.placeGuestOrder(qrToken, request));
    }

    private GuestOrderResponse forGuest(OrderResponse order) {
        return GuestOrderResponse.from(order,
            menuItemRepository.imagesOf(order.getItems().stream().map(OrderItemResponse::getMenuItemId).toList()));
    }

    private List<GuestTableResponse.Category> categories() {
        return menuCategoryRepository.findByIsActiveTrueOrderBySortOrderAscNameAsc().stream()
            .map(c -> new GuestTableResponse.Category(c.getId(), c.getName()))
            .toList();
    }

    private DiningTableEntity findTable(String qrToken) {
        return diningTableRepository.findByQrToken(qrToken)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Unknown table QR code"));
    }
}
