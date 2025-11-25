package com.example.RatingsApp.entity;

import com.example.RatingsApp.entity.enums.RatingDescription;
import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ratings")
public class Ratings {

//    @Id
//    @GenericGenerator(name = "custom-id", type = com.example.RatingsApp.config.CustomIdGenerator.class)
//    @GeneratedValue(generator = "custom-id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    // The employee being rated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id",referencedColumnName = "employeeId", nullable = false)
    private Employees employee;

    // The person who gives the rating
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rated_by_id",referencedColumnName = "employeeId", nullable = false)
    private Employees ratedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RatingRoles ratingRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RatingStatus ratingStatus;

    @Column(nullable = false)
    private int ratingValue;

    @Column(nullable = false)
    private RatingDescription ratingDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cycle_name",referencedColumnName = "cycleName" ,nullable = false)
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
