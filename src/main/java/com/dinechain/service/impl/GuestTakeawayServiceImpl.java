package com.dinechain.service.impl;

import com.dinechain.constant.BranchStatus;
import com.dinechain.constant.OrderStatus;
import com.dinechain.constant.PaymentMethod;
import com.dinechain.constant.PaymentStatus;
import com.dinechain.dto.request.WebTakeawayRequest;
import com.dinechain.dto.response.BranchLoadResponse;
import com.dinechain.dto.response.GuestOrderResponse;
import com.dinechain.dto.response.GuestTableResponse;
import com.dinechain.dto.response.PublicBranchResponse;
import com.dinechain.dto.response.WebTakeawayResponse;
import com.dinechain.entity.BranchEntity;
import com.dinechain.entity.OrderEntity;
import com.dinechain.exception.AppException;
import com.dinechain.exception.ErrorCode;
import com.dinechain.repository.BranchRepository;
import com.dinechain.repository.MenuCategoryRepository;
import com.dinechain.repository.MenuItemRepository;
import com.dinechain.entity.OrderItemEntity;
import com.dinechain.repository.OrderItemRepository;
import com.dinechain.repository.OrderRepository;
import com.dinechain.repository.PaymentRepository;
import com.dinechain.service.BranchMenuService;
import com.dinechain.service.GuestTakeawayService;
import com.dinechain.service.KitchenService;
import com.dinechain.service.OrderService;
import com.dinechain.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Takeaway ordered from the website. The tracking token is the guest's only key to their order.
 */
@Service
@RequiredArgsConstructor
public class GuestTakeawayServiceImpl implements GuestTakeawayService {

    private final BranchRepository branchRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final PaymentRepository paymentRepository;
    private final BranchMenuService branchMenuService;
    private final KitchenService kitchenService;
    private final OrderService orderService;
    private final SettingService settingService;

    @Override
    @Transactional(readOnly = true)
    public List<PublicBranchResponse> getBranches() {
        List<BranchEntity> branches = branchRepository.findAll().stream()
            .filter(b -> b.getStatus() == BranchStatus.ACTIVE)
            .sorted(Comparator.comparing(BranchEntity::getCode))
            .toList();
        Map<Integer, BranchLoadResponse> loads = kitchenService.computeLoadOfBranches(branches);
        boolean transfer = settingService.getTransferQrUrl() != null;
        return branches.stream()
            .map(b -> new PublicBranchResponse(b.getId(), b.getCode(), b.getName(), b.getAddress(),
                loads.get(b.getId()).isAcceptingTakeaway(), loads.get(b.getId()).getEstimatedWaitMinutes(), transfer,
                b.getCity(), b.getLatitude(), b.getLongitude()))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GuestTableResponse getBranchMenu(Integer branchId) {
        BranchEntity branch = branchRepository.findById(branchId)
            .filter(b -> b.getStatus() == BranchStatus.ACTIVE)
            .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));
        return new GuestTableResponse(branch.getCode(), branch.getName(), branch.getAddress(), null, null, null,
            settingService.getVatPercent(),
            menuCategoryRepository.findByIsActiveTrueOrderBySortOrderAscNameAsc().stream()
                .map(c -> new GuestTableResponse.Category(c.getId(), c.getName()))
                .toList(),
            branchMenuService.getPublicMenu(branch.getId()));
    }

    @Override
    public WebTakeawayResponse placeOrder(WebTakeawayRequest request) {
        return getOrder(orderService.placeWebTakeaway(request));
    }

    @Override
    @Transactional(readOnly = true)
    public WebTakeawayResponse getOrder(String token) {
        OrderEntity order = orderRepository.findByPublicToken(token)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Unknown takeaway order"));
        BranchEntity branch = branchRepository.findById(order.getBranchId()).orElseThrow();
        boolean paid = paymentRepository.existsByOrderIdAndStatus(order.getId(), PaymentStatus.PAID);
        // The QR is only worth showing while the guest still has to transfer
        String qr = order.getStatus() == OrderStatus.AWAITING_PAYMENT && order.getPayMethod() == PaymentMethod.BANK_TRANSFER
            ? settingService.getTransferQrUrl()
            : null;
        List<OrderItemEntity> lines = orderItemRepository.findByOrderId(order.getId());
        Map<Integer, String> images = menuItemRepository.imagesOf(lines.stream().map(OrderItemEntity::getMenuItemId).toList());
        List<GuestOrderResponse.Item> items = lines.stream()
            .map(i -> new GuestOrderResponse.Item(i.getItemName(), i.getQuantity(), i.getAmount(), i.getStatus(), i.getNote(),
                images.get(i.getMenuItemId())))
            .toList();
        return new WebTakeawayResponse(order.getPublicToken(), order.getCode(), branch.getName(), branch.getAddress(),
            order.getGuestName(), order.getStatus(), order.getPayMethod(), paid, qr,
            order.getSubtotal(), order.getVatAmount(), order.getTotalAmount(), items);
    }
}
