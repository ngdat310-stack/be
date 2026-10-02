package com.dinechain.dto.response;

import com.dinechain.constant.ReservationStatus;
import com.dinechain.entity.ReservationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Integer id;
    private Integer branchId;
    private Integer customerId;
    private Integer tableId;
    private LocalDateTime reservationDate;
    private Integer numberOfGuests;
    private ReservationStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReservationResponse from(ReservationEntity entity) {
        ReservationResponse response = new ReservationResponse();
        response.setId(entity.getId());
        response.setBranchId(entity.getBranchId());
        response.setCustomerId(entity.getCustomerId());
        response.setTableId(entity.getTableId());
        response.setStatus(entity.getStatus());
        return response;
    }
}
