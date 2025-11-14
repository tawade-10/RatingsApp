package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
// import com.example.RatingsApp.entity.enums.RatingCycles;
import com.example.RatingsApp.entity.RatingsCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingsRepo extends JpaRepository<Ratings,Long> {

    Optional<Ratings> findByRatingId(String ratingId);

    List<Ratings> findByEmployee_EmployeeIdIgnoreCase(String employeeId);

    List<Ratings> findByEmployee(Employees employee);

    List<Ratings> findByRatedBy(Employees ratedBy);

    List<Ratings> findByRatingsCycle_CycleNameIgnoreCase(String ratingsCycle);

    // Optional<Ratings> findByRatingCycles(RatingCycles ratingCycles);

   // Optional<Ratings> findByEmployeeAndRatingCycles(Employees employee, RatingCycles ratingCycles);
}
