package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
// import com.example.RatingsApp.entity.enums.RatingCycles;
import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.enums.RatingRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingsRepo extends JpaRepository<Ratings,Long> {

//    Optional<Ratings> findByRatingId(String ratingId);

    List<Ratings> findByEmployee_EmployeeId(Long employeeId);

    List<Ratings> findByEmployee(Employees employee);

    List<Ratings> findByRatedBy(Employees ratedBy);

    List<Ratings> findByRatingsCycle_CycleNameIgnoreCase(String ratingsCycle);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Ratings r " +
            "WHERE r.employee.employeeId = :employeeId " +
            "AND r.ratingRole = :ratingRole " +
            "AND r.ratingStatus = com.example.RatingsApp.entity.enums.RatingStatus.SUBMITTED_BY_TL")
    boolean isTlSubmitted(@Param("employeeId") Long employeeId,
                          @Param("ratingRole") RatingRoles ratingRole);

    // Optional<Ratings> findByRatingCycles(RatingCycles ratingCycles);

   // Optional<Ratings> findByEmployeeAndRatingCycles(Employees employee, RatingCycles ratingCycles);
}
