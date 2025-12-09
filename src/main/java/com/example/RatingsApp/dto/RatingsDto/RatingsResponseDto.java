package com.example.RatingsApp.dto.RatingsDto;

import com.example.RatingsApp.entity.Ratings;
// import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.enums.RatingDescription;
import com.example.RatingsApp.entity.enums.RatingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RatingsResponseDto {

    private String ratingId;

    private String employeeId;

    private String employeeName;

    private String ratedById;

    private String ratedByName;

    private RatingStatus ratingStatus;

    private int ratingValue;

    private RatingDescription ratingDescription;

    private String ratings_cycle;

    private LocalDate dateCreated;

    private LocalTime timeCreated;

    public RatingsResponseDto(Ratings ratings) {
        this.ratingId = ratings.getRatingId();
        this.employeeId = ratings.getEmployee().getEmployeeId();
        this.employeeName = ratings.getEmployee().getName();
        this.ratedById = ratings.getRatedBy().getEmployeeId();
        this.ratedByName = ratings.getRatedBy().getName();
        this.ratingStatus = ratings.getRatingStatus();
        this.ratingValue = ratings.getRatingValue();
        this.ratingDescription = ratings.getRatingDescription();
        this.ratings_cycle = (ratings.getRatingsCycle() != null) ? ratings.getRatingsCycle().getCycleName() : null;
        this.dateCreated = ratings.getCreatedDate();
        this.timeCreated = ratings.getCreatedTime();
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

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalTime timeCreated) {
        this.timeCreated = timeCreated;
    }
}
