package com.dinechain.service.impl;

import com.dinechain.constant.StaffStatus;
import com.dinechain.dto.request.CreateStaffRequest;
import com.dinechain.dto.request.UpdateStaffRequest;
import com.dinechain.dto.response.StaffDetailResponse;
import com.dinechain.entity.StaffEntity;
import com.dinechain.repository.StaffRepository;
import com.dinechain.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StaffDetailResponse createStaff(CreateStaffRequest request) {
        if (staffRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (staffRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        StaffEntity staff = new StaffEntity();
        staff.setCode(request.getCode());
        staff.setFullName(request.getFullName());
        staff.setUsername(request.getUsername());
        staff.setEmail(request.getEmail());
        staff.setPhone(request.getPhone());
        staff.setRole(request.getRole());
        staff.setBranchId(request.getBranchId());
        if (request.getHourlyRate() != null) {
            staff.setHourlyRate(java.math.BigDecimal.valueOf(request.getHourlyRate()));
        }
        staff.setPasswordHash(passwordEncoder.encode("DefaultPassword@123"));
        staff.setStatus(StaffStatus.ACTIVE);
        staff.setHiredAt(LocalDateTime.now());

        StaffEntity saved = staffRepository.save(staff);
        log.info("Created staff: {} ({})", saved.getFullName(), saved.getCode());
        return StaffDetailResponse.from(saved);
    }

    @Override
    public StaffDetailResponse getStaff(Integer staffId) {
        StaffEntity staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found: " + staffId));
        return StaffDetailResponse.from(staff);
    }

    @Override
    @Transactional
    public StaffDetailResponse updateStaff(Integer staffId, UpdateStaffRequest request) {
        StaffEntity staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found: " + staffId));

        staff.setFullName(request.getFullName());
        staff.setEmail(request.getEmail());
        staff.setPhone(request.getPhone());
        staff.setRole(request.getRole());
        if (request.getHourlyRate() != null) {
            staff.setHourlyRate(java.math.BigDecimal.valueOf(request.getHourlyRate()));
        }
        staff.setUpdatedAt(LocalDateTime.now());

        StaffEntity updated = staffRepository.save(staff);
        log.info("Updated staff: {} ({})", updated.getFullName(), updated.getCode());
        return StaffDetailResponse.from(updated);
    }

    @Override
    @Transactional
    public void deleteStaff(Integer staffId) {
        StaffEntity staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found: " + staffId));

        staff.setStatus(StaffStatus.RESIGNED);
        staff.setUpdatedAt(LocalDateTime.now());
        staffRepository.save(staff);
        log.info("Deactivated staff: {} ({})", staff.getFullName(), staff.getCode());
    }

    @Override
    public List<StaffDetailResponse> getStaffByBranch(Integer branchId) {
        return staffRepository.findByBranchIdAndStatus(branchId, StaffStatus.ACTIVE)
            .stream()
            .map(StaffDetailResponse::from)
            .toList();
    }

    @Override
    public List<StaffDetailResponse> searchStaff(String fullName) {
        return staffRepository.findByFullNameContainingIgnoreCase(fullName)
            .stream()
            .filter(s -> s.getStatus() == StaffStatus.ACTIVE)
            .map(StaffDetailResponse::from)
            .toList();
    }
}
