package com.dinechain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Auth errors
    UNAUTHORIZED("UNAUTHORIZED", "Invalid or missing authentication token", 401),
    FORBIDDEN("FORBIDDEN", "Access denied", 403),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid username or password", 401),
    ACCOUNT_RESIGNED("ACCOUNT_RESIGNED", "Staff has resigned", 403),
    INVALID_TOKEN("INVALID_TOKEN", "Invalid or expired token", 401),
    REFRESH_TOKEN_REVOKED("REFRESH_TOKEN_REVOKED", "Refresh token has been revoked", 401),

    // Validation errors
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation failed", 400),
    INVALID_REQUEST("INVALID_REQUEST", "Invalid request", 400),

    // Resource not found
    NOT_FOUND("NOT_FOUND", "Resource not found", 404),
    STAFF_NOT_FOUND("STAFF_NOT_FOUND", "Staff not found", 404),
    BRANCH_NOT_FOUND("BRANCH_NOT_FOUND", "Branch not found", 404),

    // Conflict errors
    CONFLICT("CONFLICT", "Resource already exists", 409),
    DUPLICATE_USERNAME("DUPLICATE_USERNAME", "Username already exists", 409),
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "Email already exists", 409),
    DUPLICATE_CODE("DUPLICATE_CODE", "Code already exists", 409),

    // Invalid state
    INVALID_STATE("INVALID_STATE", "Operation not allowed in current state", 409),
    TABLE_OCCUPIED("TABLE_OCCUPIED", "Table is occupied", 409),

    // Server errors
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error", 500);

    private final String code;
    private final String message;
    private final int httpStatus;

    ErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
