package com.example.RatingsApp.entity;

import com.example.RatingsApp.entity.enums.RatingDescription;
import com.example.RatingsApp.entity.enums.RatingTypes;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Ratings {

//    @Id
//    @GenericGenerator(name = "custom-id", type = com.example.RatingsApp.config.CustomIdGenerator.class)
//    @GeneratedValue(generator = "custom-id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employees employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rated_by_id", nullable = false)
    @JsonIgnore
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
    @JsonIgnore
    private RatingsCycle ratingsCycle;

    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
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
}
