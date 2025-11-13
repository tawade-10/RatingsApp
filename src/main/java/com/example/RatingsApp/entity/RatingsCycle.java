//package com.example.RatingsApp.entity;
//
//import com.example.RatingsApp.entity.enums.CycleStatus;
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Entity
//@Table(name = "ratings_cycle")
//public class RatingsCycle {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String cycleId;
//
//    @Column(nullable = false)
//    private String cycleName;
//
//    @Column(nullable = false)
//    private LocalDate startDate;
//
//    @Column(nullable = false)
//    private LocalDate endDate;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private CycleStatus cycleStatus;
//
//    @OneToMany(mappedBy = "ratingCycle", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Ratings> ratings;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getCycleId() {
//        return cycleId;
//    }
//
//    public void setCycleId(String cycleId) {
//        this.cycleId = cycleId;
//    }
//
//    public String getCycleName() {
//        return cycleName;
//    }
//
//    public void setCycleName(String cycleName) {
//        this.cycleName = cycleName;
//    }
//
//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(LocalDate startDate) {
//        this.startDate = startDate;
//    }
//
//    public LocalDate getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(LocalDate endDate) {
//        this.endDate = endDate;
//    }
//
//    public CycleStatus getCycleStatus() {
//        return cycleStatus;
//    }
//
//    public void setCycleStatus(CycleStatus cycleStatus) {
//        this.cycleStatus = cycleStatus;
//    }
//
//    public List<Ratings> getRatings() {
//        return ratings;
//    }
//
//    public void setRatings(List<Ratings> ratings) {
//        this.ratings = ratings;
//    }
//}
