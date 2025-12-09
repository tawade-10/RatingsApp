package com.example.RatingsApp.entity;

import com.example.RatingsApp.entity.enums.CycleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ratings_cycle")
public class RatingsCycle {

    @Id
    @GeneratedValue(generator = "cycle-id-generator")
    @GenericGenerator(name = "cycle-id-generator",
            strategy = "com.example.RatingsApp.config.CustomIdGenerator")
    private String cycleId;

    @Column(nullable = false)
    private String cycleName;

    @Enumerated(EnumType.STRING)
    private CycleStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "ratingsCycle", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Ratings> ratings;

    public String getCycleId() {
        return cycleId;
    }

    public void setCycleId(String cycleId) {
        this.cycleId = cycleId;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public CycleStatus getStatus() {
        return status;
    }

    public void setStatus(CycleStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Ratings> getRatings() {
        return ratings;
    }

    public void setRatings(List<Ratings> ratings) {
        this.ratings = ratings;
    }
}
