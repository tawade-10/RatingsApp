package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamsRepo extends JpaRepository<Teams,Long> {
    Optional<Teams> findByTeamNameIgnoreCase(String teamName);

    Optional<Teams> findByPm(Employees pm);
}
