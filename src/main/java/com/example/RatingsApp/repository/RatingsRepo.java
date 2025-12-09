package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
// import com.example.RatingsApp.entity.enums.RatingCycles;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.example.RatingsApp.entity.enums.RatingTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingsRepo extends JpaRepository<Ratings,Long> {

    List<Ratings> findByEmployee(Employees employee);

    List<Ratings> findByRatedBy(Employees ratedBy);

    List<Ratings> findByRatingsCycle_CycleNameIgnoreCase(String ratingsCycle);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Ratings r " +
            "WHERE r.employee.employeeId = :employeeId " +
            "AND r.ratingTypes = :ratingTypes " +
            "AND r.ratingStatus = com.example.RatingsApp.entity.enums.RatingStatus.SUBMITTED_BY_TL")
    boolean isTlSubmitted(@Param("employeeId") String employeeId,
                          @Param("ratingTypes") RatingTypes ratingTypes);

    @Query("SELECT r " +
            "FROM Ratings r WHERE r.employee.employeeId = :employeeId " +
            "AND r.ratedBy.employeeId = :ratedById " +
            "AND r.ratingTypes = :ratingTypes " +
            "AND r.ratingsCycle.cycleId = :cycleId")
    Optional<Ratings> findExistingRating(String ratedById, String employeeId, RatingTypes ratingTypes, String cycleId);

    Optional<Ratings> findByRatingId(String ratingId);

    List<Ratings> findByRatingStatus(RatingStatus ratingStatus);

    List<Ratings> findByEmployeeIn(List<Employees> employees);

    List<Ratings> findByRatingTypes(RatingTypes ratingTypes);
}
