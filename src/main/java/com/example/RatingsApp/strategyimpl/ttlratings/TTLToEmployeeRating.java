package com.example.RatingsApp.strategyimpl.ttlratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("TTL_TO_EMPLOYEE")
public class TTLToEmployeeRating implements RatingStrategy {

    private final EmployeesRepo employeesRepo;

    public TTLToEmployeeRating(EmployeesRepo employeesRepo) {
        this.employeesRepo = employeesRepo;
    }

    @Override
    public Ratings giveRating(RatingsRequestDto ratingsRequestDto) {

        Long ttlId = ratingsRequestDto.getRated_by_id();
        Long empId = ratingsRequestDto.getEmployee_id();

        if(ttlId == null || empId == null){
            throw new ResourceNotFoundException("Both TTL ID and Employee ID must be provided.");
        }

        Employees ttl = employeesRepo.findById(ttlId)
                .orElseThrow(() -> new ResourceNotFoundException("TTL not found with ID: " + ttlId));

        if(ttl.getRole() == null || ttl.getRole().getRoleId() != 2L){
            throw new APIException("Only TTL can give this rating.");
        }

        Employees emp = employeesRepo.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + empId));

        if(emp.getRole() == null || emp.getRole().getRoleId() != 4L){
            throw new APIException("Only Employee can receive this rating.");
        }

        Ratings ratings = new Ratings();

        ratings.setRatingRole(ratingsRequestDto.getRating_role());
        ratings.setRatingValue(ratingsRequestDto.getRating_value());
        ratings.setRatingStatus(ratingsRequestDto.getRating_status());

        return ratings;
    }
}
