package com.example.RatingsApp.entity;

import com.example.RatingsApp.entity.enums.CycleStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ratings_cycle")
public class RatingsCycle {

//    @Id
//    @GenericGenerator(name = "custom-id", type = com.example.RatingsApp.config.CustomIdGenerator.class)
//    @GeneratedValue(generator = "custom-id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cycleId;

    @Column(nullable = false, unique = false)
    private String cycleName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CycleStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "ratingsCycle",fetch = FetchType.LAZY)
    private List<Ratings> ratings;

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
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
