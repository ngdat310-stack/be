package com.dinechain.service.impl;

import com.dinechain.dto.response.BranchMenuItemResponse;
import com.dinechain.entity.BranchMenuItemEntity;
import com.dinechain.entity.MenuItemEntity;
import com.dinechain.exception.AppException;
import com.dinechain.exception.ErrorCode;
import com.dinechain.repository.BranchMenuItemRepository;
import com.dinechain.repository.BranchRepository;
import com.dinechain.repository.MenuItemRepository;
import com.dinechain.security.BranchAccessGuard;
import com.dinechain.service.BranchMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The chain menu as one branch sells it: a dish can be sold out at a branch (e.g. ran out at peak hours)
 * or carry a branch price; anything not overridden follows the chain menu.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BranchMenuServiceImpl implements BranchMenuService {

    private final MenuItemRepository menuItemRepository;
    private final BranchMenuItemRepository branchMenuItemRepository;
    private final BranchRepository branchRepository;
    private final BranchAccessGuard branchAccessGuard;

    @Override
    @Transactional(readOnly = true)
    public List<BranchMenuItemResponse> getMenu(Integer staffId, Integer branchId) {
        checkBranch(staffId, branchId);
        Map<Integer, BranchMenuItemEntity> overrides = branchMenuItemRepository.findByBranchId(branchId).stream()
            .collect(Collectors.toMap(BranchMenuItemEntity::getMenuItemId, Function.identity()));
        return menuItemRepository.findByIsActiveTrueAndDeletedAtIsNullOrderByCategoryIdAscNameAsc().stream()
            .map(item -> toResponse(item, overrides.get(item.getId())))
            .toList();
    }

    @Override
    @Transactional
    public BranchMenuItemResponse setAvailability(Integer staffId, Integer branchId, Integer menuItemId, boolean available) {
        checkBranch(staffId, branchId);
        MenuItemEntity item = findDish(menuItemId);
        BranchMenuItemEntity entry = entryFor(branchId, menuItemId);
        entry.setIsAvailable(available);
        branchMenuItemRepository.save(entry);
        log.info("Branch {}: {} is now {}", branchId, item.getName(), available ? "available" : "sold out");
        return toResponse(item, entry);
    }

    @Override
    @Transactional
    public BranchMenuItemResponse setPrice(Integer staffId, Integer branchId, Integer menuItemId, BigDecimal price) {
        checkBranch(staffId, branchId);
        MenuItemEntity item = findDish(menuItemId);
        BranchMenuItemEntity entry = entryFor(branchId, menuItemId);
        entry.setPrice(price);
        branchMenuItemRepository.save(entry);
        log.info("Branch {}: {} price set to {}", branchId, item.getName(), price != null ? price : "chain price");
        return toResponse(item, entry);
    }

    private void checkBranch(Integer staffId, Integer branchId) {
        branchAccessGuard.check(staffId, branchId);
        if (!branchRepository.existsById(branchId)) {
            throw new AppException(ErrorCode.BRANCH_NOT_FOUND);
        }
    }

    private MenuItemEntity findDish(Integer menuItemId) {
        return menuItemRepository.findById(menuItemId)
            .filter(item -> Boolean.TRUE.equals(item.getIsActive()) && item.getDeletedAt() == null)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Dish not on the chain menu: " + menuItemId));
    }

    private BranchMenuItemEntity entryFor(Integer branchId, Integer menuItemId) {
        return branchMenuItemRepository.findByBranchIdAndMenuItemId(branchId, menuItemId).orElseGet(() -> {
            BranchMenuItemEntity created = new BranchMenuItemEntity();
            created.setBranchId(branchId);
            created.setMenuItemId(menuItemId);
            created.setIsAvailable(true);
            return created;
        });
    }

    private static BranchMenuItemResponse toResponse(MenuItemEntity item, BranchMenuItemEntity override) {
        BigDecimal branchPrice = override != null ? override.getPrice() : null;
        boolean available = override == null || Boolean.TRUE.equals(override.getIsAvailable());
        return new BranchMenuItemResponse(item.getId(), item.getCategoryId(), item.getCode(), item.getName(), item.getUnit(),
            item.getBasePrice(), branchPrice, branchPrice != null ? branchPrice : item.getBasePrice(), available);
    }
}
