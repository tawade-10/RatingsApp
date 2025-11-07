package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingsRepo extends JpaRepository<Ratings,Long> {

    Optional<Ratings> findByRatingId(String ratingId);

    Optional<Ratings> findByRatingCycles(String ratingCycles);
}
