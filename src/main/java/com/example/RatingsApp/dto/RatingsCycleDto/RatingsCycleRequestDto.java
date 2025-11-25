package com.example.RatingsApp.dto.RatingsCycleDto;

import com.example.RatingsApp.entity.enums.CycleStatus;

import java.time.LocalDate;

public class RatingsCycleRequestDto {

//    private String cycleId;

    private CycleStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    public RatingsCycleRequestDto(CycleStatus status, LocalDate startDate, LocalDate endDate) {
//        this.cycleId = cycleId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

//    public String getCycleId() {
//        return cycleId;
//    }
//
//    public void setCycleId(String cycleId) {
//        this.cycleId = cycleId;
//    }

    public CycleStatus getStatus() {
        return status;
    }

    public void setStatus(CycleStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
