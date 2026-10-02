package com.dinechain.dto.response;

import com.dinechain.constant.BranchStatus;
import com.dinechain.entity.BranchEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {
    private Integer id;
    private String code;
    private String name;
    private String address;
    private String city;
    private String phone;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Integer managerId;
    private BranchStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BranchResponse from(BranchEntity entity) {
        BranchResponse response = new BranchResponse();
        response.setId(entity.getId());
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setAddress(entity.getAddress());
        response.setCity(entity.getCity());
        response.setPhone(entity.getPhone());
        response.setOpenTime(entity.getOpenTime());
        response.setCloseTime(entity.getCloseTime());
        response.setManagerId(entity.getManagerId());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
