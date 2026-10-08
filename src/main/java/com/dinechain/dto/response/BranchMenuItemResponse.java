package com.dinechain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchMenuItemResponse {
    private Integer menuItemId;
    private Integer categoryId;
    private String code;
    private String name;
    private String unit;
    // Chain price
    private BigDecimal basePrice;
    // Branch's own price, null when it sells at the chain price
    private BigDecimal branchPrice;
    // What the customer pays here
    private BigDecimal price;
    private boolean available;
}
