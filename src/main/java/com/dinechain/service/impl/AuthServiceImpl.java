package com.dinechain.service.impl;

import com.dinechain.constant.StaffStatus;
import com.dinechain.dto.request.ChangePasswordRequest;
import com.dinechain.dto.request.LoginRequest;
import com.dinechain.dto.request.RefreshTokenRequest;
import com.dinechain.dto.response.AuthResponse;
import com.dinechain.dto.response.StaffResponse;
import com.dinechain.entity.RefreshTokenEntity;
import com.dinechain.entity.StaffEntity;
import com.dinechain.repository.RefreshTokenRepository;
import com.dinechain.repository.StaffRepository;
import com.dinechain.security.JwtService;
import com.dinechain.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final StaffRepository staffRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        // 1. Find staff by username
        StaffEntity staff = staffRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // 2. Check if staff is resigned
        if (staff.getStatus() == StaffStatus.RESIGNED) {
            throw new RuntimeException("Staff has resigned");
        }

        // 3. Verify password
        if (!passwordEncoder.matches(request.getPassword(), staff.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 4. Generate tokens
        String accessToken = jwtService.generateAccessToken(staff);
        String refreshToken = jwtService.generateRefreshToken(staff);

        // 5. Save refresh token in database
        LocalDateTime expiresAt = LocalDateTime.now()
            .plusSeconds(7 * 24 * 60 * 60); // 7 days

        RefreshTokenEntity tokenEntity = RefreshTokenEntity.builder()
            .staffId(staff.getId())
            .tokenHash(hashToken(refreshToken))
            .expiresAt(expiresAt)
            .createdAt(LocalDateTime.now())
            .build();

        refreshTokenRepository.save(tokenEntity);

        return new AuthResponse(
            accessToken,
            refreshToken,
            jwtService.getAccessTokenExpirationSeconds(),
            StaffResponse.from(staff)
        );
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        // 1. Validate refresh token
        Integer staffId = jwtService.validateRefreshToken(request.getRefreshToken());

        // 2. Check if token is revoked in database
        RefreshTokenEntity token = refreshTokenRepository
            .findByTokenHashAndRevokedAtIsNull(hashToken(request.getRefreshToken()))
            .orElseThrow(() -> new RuntimeException("Invalid or revoked refresh token"));

        // 3. Get staff
        StaffEntity staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (staff.getStatus() == StaffStatus.RESIGNED) {
            throw new RuntimeException("Staff has resigned");
        }

        // 4. Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(staff);
        String newRefreshToken = jwtService.generateRefreshToken(staff);

        // 5. Save new refresh token
        LocalDateTime expiresAt = LocalDateTime.now()
            .plusSeconds(7 * 24 * 60 * 60);

        RefreshTokenEntity newToken = RefreshTokenEntity.builder()
            .staffId(staff.getId())
            .tokenHash(hashToken(newRefreshToken))
            .expiresAt(expiresAt)
            .createdAt(LocalDateTime.now())
            .build();

        refreshTokenRepository.save(newToken);

        // 6. Optionally revoke old token
        refreshTokenRepository.revokeByTokenHash(
            hashToken(request.getRefreshToken()),
            LocalDateTime.now()
        );

        AuthResponse response = new AuthResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(jwtService.getAccessTokenExpirationSeconds());
        response.setStaff(StaffResponse.from(staff));
        return response;
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.revokeByTokenHash(
            hashToken(refreshToken),
            LocalDateTime.now()
        );
    }

    @Override
    public StaffResponse getCurrentStaff(Integer staffId) {
        StaffEntity staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found"));
        return StaffResponse.from(staff);
    }

    @Override
    @Transactional
    public void changePassword(Integer staffId, ChangePasswordRequest request) {
        StaffEntity staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), staff.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Update password
        staff.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        staffRepository.save(staff);

        // Revoke all tokens (force re-login)
        refreshTokenRepository.revokeAllTokensByStaffId(staffId, LocalDateTime.now());
    }

    private String hashToken(String token) {
        // TODO: Implement token hashing (SHA-256 or similar)
        return token; // Temporary: use token as-is
    }
}
