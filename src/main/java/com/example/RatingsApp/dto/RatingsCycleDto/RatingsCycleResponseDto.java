package com.example.RatingsApp.dto.RatingsCycleDto;

import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.enums.CycleStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class RatingsCycleResponseDto {

    private String cycleId;

    private String cycleName;

    private CycleStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    public RatingsCycleResponseDto(RatingsCycle ratingsCycle) {
        this.cycleId = ratingsCycle.getCycleId();
        this.cycleName = ratingsCycle.getCycleName();
        this.status = ratingsCycle.getStatus();
        this.startDate = ratingsCycle.getStartDate();
        this.endDate = ratingsCycle.getEndDate();
    }

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

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
