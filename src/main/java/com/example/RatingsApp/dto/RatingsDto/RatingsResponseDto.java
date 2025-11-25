package com.example.RatingsApp.dto.RatingsDto;

import com.example.RatingsApp.entity.Ratings;
// import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.enums.RatingDescription;
import com.example.RatingsApp.entity.enums.RatingStatus;

public class RatingsResponseDto {

    private Long ratingId;

    private Long employeeId;

    private String employeeName;

    private Long ratedById;

    private String ratedByName;

    private RatingStatus ratingStatus;

    private int ratingValue;

    private RatingDescription ratingDescription;

    private String ratings_cycle;

    public RatingsResponseDto(Ratings rating) {
        this.ratingId = rating.getRatingId();
        this.employeeId = rating.getEmployee().getEmployeeId();
        this.employeeName = rating.getEmployee().getName();
        this.ratedById = rating.getRatedBy().getEmployeeId();
        this.ratedByName = rating.getRatedBy().getName();
        this.ratingStatus = rating.getRatingStatus();
        this.ratingValue = rating.getRatingValue();
        this.ratingDescription = rating.getRatingDescription();
        this.ratings_cycle = (rating.getRatingsCycle() != null) ? rating.getRatingsCycle().getCycleName() : null;
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

    public RatingDescription getRatingDescription() {
        return ratingDescription;
    }

    public void setRatingDescription(RatingDescription ratingDescription) {
        this.ratingDescription = ratingDescription;
    }

    public String getRatings_cycle() {
        return ratings_cycle;
    }

    public void setRatings_cycle(String ratings_cycle) {
        this.ratings_cycle = ratings_cycle;
    }
}
