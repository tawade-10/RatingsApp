package com.example.RatingsApp.dto.RatingsDto;

import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.enums.RatingCycles;
import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;

public class RatingsResponseDto {

    private Long id;

    private String ratingId;

    private String employeeId;

    private String employeeName;

    private String ratedById;

    private String ratedByName;

    private RatingRoles ratingRole;

    private RatingStatus ratingStatus;

    private int ratingValue;

    private RatingCycles ratingCycles;


    public RatingsResponseDto(Ratings rating) {
        this.id = rating.getRatedBy().getId();
        this.ratingId = rating.getRatingId();
        this.employeeId = rating.getEmployee().getEmployeeId();
        this.employeeName = rating.getEmployee().getName();
        this.ratedById = rating.getRatedBy().getEmployeeId();
        this.ratedByName = rating.getRatedBy().getName();
        this.ratingRole = rating.getRatingRole();
        this.ratingStatus = rating.getRatingStatus();
        this.ratingValue = rating.getRatingValue();
        this.ratingCycles = rating.getRatingCycles();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getRatedById() {
        return ratedById;
    }

    public void setRatedById(String ratedById) {
        this.ratedById = ratedById;
    }

    public String getRatedByName() {
        return ratedByName;
    }

    public void setRatedByName(String ratedByName) {
        this.ratedByName = ratedByName;
    }

    public RatingRoles getRatingRole() {
        return ratingRole;
    }

    public void setRatingRole(RatingRoles ratingRole) {
        this.ratingRole = ratingRole;
    }

    public RatingStatus getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(RatingStatus ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public RatingCycles getRatingCycles() {
        return ratingCycles;
    }

    public void setRatingCycles(RatingCycles ratingCycles) {
        this.ratingCycles = ratingCycles;
    }
}
