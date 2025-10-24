package com.example.RatingsApp.dto.RatingsDto;

import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;

public class RatingsResponseDto {

    private Long ratingId;

    private Long employeeId;

    private String employeeName;

    private Long ratedById;

    private String ratedByName;

    private RatingRoles ratingRole;

    private RatingStatus ratingStatus;

    private int ratingValue;


    public RatingsResponseDto(Ratings rating) {
        this.ratingId = rating.getRatingId();
        this.employeeId = rating.getEmployee().getEmployeeId();
        this.employeeName = rating.getEmployee().getName();
        this.ratedById = rating.getRatedBy().getEmployeeId();
        this.ratedByName = rating.getRatedBy().getName();
        this.ratingRole = rating.getRatingRole();
        this.ratingStatus = rating.getRatingStatus();
        this.ratingValue = rating.getRatingValue();
    }


    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getRatedById() {
        return ratedById;
    }

    public void setRatedById(Long ratedById) {
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
}
