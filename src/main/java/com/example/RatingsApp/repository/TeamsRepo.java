package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepo extends JpaRepository<Teams,Long> {
}
