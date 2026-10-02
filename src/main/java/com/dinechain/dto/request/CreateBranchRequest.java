package com.dinechain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBranchRequest {
    @NotBlank(message = "Branch code is required")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Branch code must be uppercase alphanumeric with underscores")
    private String code;

    @NotBlank(message = "Branch name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @Pattern(regexp = "^[0-9\\-+\\s()]+$", message = "Phone number is invalid")
    private String phone;

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    private Integer managerId;
}
