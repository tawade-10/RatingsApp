package com.example.RatingsApp.entity;

import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Ratings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employee;

    @ManyToOne
    @JoinColumn(name = "rated_by_id", nullable = false)
    private Employees ratedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RatingRoles ratingRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RatingStatus ratingStatus;

    @Column(nullable = false)
    private int ratingValue;

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
