package com.example.RatingsApp.entity;

import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Ratings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ratingId;

    // The employee being rated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employee;

    // The person who gives the rating
    @ManyToOne(fetch = FetchType.LAZY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_name", nullable = false)
    private RatingsCycle ratingsCycle;

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

    public RatingsCycle getRatingsCycle() {
        return ratingsCycle;
    }

    public void setRatingsCycle(RatingsCycle ratingsCycle) {
        this.ratingsCycle = ratingsCycle;
    }
}
