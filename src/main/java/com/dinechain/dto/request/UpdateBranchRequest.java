package com.dinechain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBranchRequest {
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

    @Positive(message = "Kitchen capacity must be positive")
    private Integer kitchenCapacity;
}
