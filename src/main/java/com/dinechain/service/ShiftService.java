package com.dinechain.service;

import com.dinechain.dto.request.AssignShiftRequest;
import com.dinechain.dto.request.CreateShiftRequest;
import com.dinechain.dto.response.ShiftResponse;
import com.dinechain.dto.response.StaffShiftResponse;
import com.dinechain.dto.response.TimesheetResponse;

import java.time.LocalDate;
import java.util.List;

public interface ShiftService {
    ShiftResponse createShift(Integer actorId, Integer branchId, CreateShiftRequest request);
    List<ShiftResponse> getShifts(Integer actorId, Integer branchId);
    StaffShiftResponse assign(Integer actorId, Integer shiftId, AssignShiftRequest request);
    void unassign(Integer actorId, Integer assignmentId);
    List<StaffShiftResponse> getSchedule(Integer actorId, Integer branchId, LocalDate from, LocalDate to);
    List<StaffShiftResponse> getMySchedule(Integer actorId, LocalDate from, LocalDate to);
    StaffShiftResponse checkIn(Integer actorId, Integer assignmentId);
    StaffShiftResponse checkOut(Integer actorId, Integer assignmentId);
    List<TimesheetResponse> getTimesheet(Integer actorId, Integer branchId, LocalDate from, LocalDate to);
}
