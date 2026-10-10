package com.dinechain.service;

import com.dinechain.dto.request.AreaRequest;
import com.dinechain.dto.request.TableRequest;
import com.dinechain.dto.response.AreaResponse;
import com.dinechain.dto.response.TableResponse;

import java.util.List;

public interface FloorService {
    List<AreaResponse> getAreas(Integer staffId, Integer branchId);
    AreaResponse createArea(Integer staffId, Integer branchId, AreaRequest request);
    AreaResponse renameArea(Integer staffId, Integer areaId, AreaRequest request);
    void deleteArea(Integer staffId, Integer areaId);
    List<TableResponse> getTables(Integer staffId, Integer branchId);
    TableResponse createTable(Integer staffId, Integer areaId, TableRequest request);
    TableResponse updateTable(Integer staffId, Integer tableId, TableRequest request);
    void deleteTable(Integer staffId, Integer tableId);
}
