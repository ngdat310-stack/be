package com.dinechain.service;

import com.dinechain.dto.request.ChangePasswordRequest;
import com.dinechain.dto.request.LoginRequest;
import com.dinechain.dto.request.RefreshTokenRequest;
import com.dinechain.dto.response.AuthResponse;
import com.dinechain.dto.response.StaffResponse;

public interface AuthService {
    /**
     * Staff login with username and password
     */
    AuthResponse login(LoginRequest request);

    /**
     * Refresh access token using refresh token
     */
    AuthResponse refresh(RefreshTokenRequest request);

    /**
     * Logout (revoke refresh token)
     */
    void logout(String refreshToken);

    /**
     * Get current logged-in staff info
     */
    StaffResponse getCurrentStaff(Integer staffId);

    /**
     * Change password for current staff
     */
    void changePassword(Integer staffId, ChangePasswordRequest request);
}
