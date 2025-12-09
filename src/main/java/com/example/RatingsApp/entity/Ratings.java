package com.example.RatingsApp.entity;

import com.example.RatingsApp.entity.enums.RatingDescription;
import com.example.RatingsApp.entity.enums.RatingTypes;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "ratings")
public class Ratings {

    @Id
    @GeneratedValue(generator = "rating-id-generator")
    @GenericGenerator(name = "rating-id-generator",
            strategy = "com.example.RatingsApp.config.CustomIdGenerator")
    private String ratingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rated_by_id", nullable = false)
    private Employees ratedBy;

    @Enumerated(EnumType.STRING)
    private RatingTypes ratingTypes;

    @Enumerated(EnumType.STRING)
    private RatingStatus ratingStatus;

    private int ratingValue;

    @Enumerated(EnumType.STRING)
    private RatingDescription ratingDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_id", nullable = false)
    private RatingsCycle ratingsCycle;

    @Column(updatable = false)
    private LocalDate createdDate;

    @Column(updatable = false)
    private LocalTime createdTime;

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public Employees getRatedBy() {
        return ratedBy;
    }

    public void setRatedBy(Employees ratedBy) {
        this.ratedBy = ratedBy;
    }

    public RatingTypes getRatingTypes() {
        return ratingTypes;
    }

    public void setRatingTypes(RatingTypes ratingTypes) {
        this.ratingTypes = ratingTypes;
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

    public RatingsCycle getRatingsCycle() {
        return ratingsCycle;
    }

    public void setRatingsCycle(RatingsCycle ratingsCycle) {
        this.ratingsCycle = ratingsCycle;
    }

    @PrePersist
    public void onCreate() {
        this.createdDate = LocalDate.now();
        this.createdTime = LocalTime.now();
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalTime createdTime) {
        this.createdTime = createdTime;
    }
}
