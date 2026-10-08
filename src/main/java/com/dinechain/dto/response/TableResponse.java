package com.dinechain.dto.response;

import com.dinechain.constant.TableStatus;
import com.dinechain.entity.DiningTableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableResponse {
    private Integer id;
    private Integer areaId;
    private String name;
    private Integer capacity;
    private TableStatus status;

    public static TableResponse from(DiningTableEntity entity) {
        return new TableResponse(entity.getId(), entity.getAreaId(), entity.getName(), entity.getCapacity(), entity.getStatus());
    }
}
