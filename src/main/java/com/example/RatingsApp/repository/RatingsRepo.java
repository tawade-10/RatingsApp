package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingsRepo extends JpaRepository<Ratings,Long> {
}
