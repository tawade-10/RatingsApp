package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.RatingsCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.OptionalDouble;

public interface RatingsCycleRepo extends JpaRepository<RatingsCycle, Long> {
    Optional<RatingsCycle> findByCycleNameIgnoreCase(String ratingsCycle);

    Optional<RatingsCycle> findByStartDate(LocalDate startDate);

    Optional<RatingsCycle> findByEndDate(LocalDate endDate);

    Optional<RatingsCycle> findByCycleId(String cycleId);

    RatingsCycle findFirstByStartDateAfterOrderByStartDateAsc(LocalDate endDate);
}
