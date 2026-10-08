package com.dinechain.service.impl;

import com.dinechain.constant.BranchStatus;
import com.dinechain.constant.KitchenLoadStatus;
import com.dinechain.constant.OrderStatus;
import com.dinechain.dto.response.BranchLoadResponse;
import com.dinechain.dto.response.ChainDashboardResponse;
import com.dinechain.entity.BranchEntity;
import com.dinechain.repository.BranchRepository;
import com.dinechain.repository.OrderRepository;
import com.dinechain.repository.PaymentRepository;
import com.dinechain.service.DashboardService;
import com.dinechain.service.KitchenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final BranchRepository branchRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final KitchenService kitchenService;

    // Fixed number of queries no matter how many branches the chain has
    @Override
    @Transactional(readOnly = true)
    public ChainDashboardResponse getChainDashboard() {
        List<BranchEntity> branches = branchRepository.findByStatus(BranchStatus.ACTIVE);

        Map<Integer, Long> openOrders = new HashMap<>();
        for (Object[] row : orderRepository.countByStatusGroupByBranch(OrderStatus.OPEN)) {
            openOrders.put((Integer) row[0], ((Number) row[1]).longValue());
        }
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Map<Integer, Long> completedToday = new HashMap<>();
        for (Object[] row : orderRepository.countCompletedSinceGroupByBranch(startOfDay)) {
            completedToday.put((Integer) row[0], ((Number) row[1]).longValue());
        }
        // Revenue = cash actually taken today (refunds excluded)
        Map<Integer, BigDecimal> revenueToday = new HashMap<>();
        for (Object[] row : paymentRepository.sumPaidSinceGroupByBranch(startOfDay)) {
            revenueToday.put((Integer) row[0], (BigDecimal) row[1]);
        }
        Map<Integer, BranchLoadResponse> loads = kitchenService.computeLoadOfBranches(branches);

        List<ChainDashboardResponse.BranchSummary> summaries = branches.stream()
            .map(branch -> new ChainDashboardResponse.BranchSummary(
                branch.getId(),
                branch.getCode(),
                branch.getName(),
                openOrders.getOrDefault(branch.getId(), 0L),
                completedToday.getOrDefault(branch.getId(), 0L),
                revenueToday.getOrDefault(branch.getId(), BigDecimal.ZERO),
                loads.get(branch.getId())))
            .toList();

        return new ChainDashboardResponse(
            LocalDateTime.now(),
            summaries.stream().mapToLong(ChainDashboardResponse.BranchSummary::getOpenOrders).sum(),
            summaries.stream().mapToLong(ChainDashboardResponse.BranchSummary::getCompletedOrdersToday).sum(),
            summaries.stream().map(ChainDashboardResponse.BranchSummary::getRevenueToday).reduce(BigDecimal.ZERO, BigDecimal::add),
            summaries.stream().filter(s -> s.getKitchen().getStatus() == KitchenLoadStatus.OVERLOADED).count(),
            summaries
        );
    }
}
