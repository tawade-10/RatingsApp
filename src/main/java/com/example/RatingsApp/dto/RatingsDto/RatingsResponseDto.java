package com.example.RatingsApp.dto.RatingsDto;

import com.example.RatingsApp.entity.Ratings;
// import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.enums.RatingStatus;

public class RatingsResponseDto {

    private Long id;

    private String ratingId;

    private String employeeId;

    private String employeeName;

    private String ratedById;

    private String ratedByName;

    private RatingStatus ratingStatus;

    private int ratingValue;

    private String ratings_cycle;

    public RatingsResponseDto(Ratings rating) {
        this.id = rating.getId();
        this.ratingId = rating.getRatingId();
        this.employeeId = rating.getEmployee().getEmployeeId();
        this.employeeName = rating.getEmployee().getName();
        this.ratedById = rating.getRatedBy().getEmployeeId();
        this.ratedByName = rating.getRatedBy().getName();
        this.ratingStatus = rating.getRatingStatus();
        this.ratingValue = rating.getRatingValue();
        this.ratings_cycle = (rating.getRatingsCycle() != null) ? rating.getRatingsCycle().getCycleId() : null;
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

    public String getRatings_cycle() {
        return ratings_cycle;
    }

    public void setRatings_cycle(String ratings_cycle) {
        this.ratings_cycle = ratings_cycle;
    }
}
