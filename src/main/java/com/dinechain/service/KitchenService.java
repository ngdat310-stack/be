package com.dinechain.service;

import com.dinechain.dto.response.BranchLoadResponse;
import com.dinechain.dto.response.KitchenTicketResponse;
import com.dinechain.entity.BranchEntity;

import java.util.List;
import java.util.Map;

public interface KitchenService {
    List<KitchenTicketResponse> getQueue(Integer staffId, Integer branchId);
    KitchenTicketResponse startCooking(Integer staffId, Integer itemId);
    KitchenTicketResponse markReady(Integer staffId, Integer itemId);
    KitchenTicketResponse markServed(Integer staffId, Integer itemId);
    BranchLoadResponse getLoad(Integer staffId, Integer branchId);
    BranchLoadResponse computeLoad(BranchEntity branch);
    Map<Integer, BranchLoadResponse> computeLoadOfBranches(List<BranchEntity> branches);
}
