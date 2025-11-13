package com.example.RatingsApp.strategyimpl.tlratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.stereotype.Component;

@Component("TL_TO_EMPLOYEE")
public class TLToIndividualRating implements RatingStrategy {

    private final EmployeesRepo employeesRepo;

    public TLToIndividualRating(EmployeesRepo employeesRepo) {
        this.employeesRepo = employeesRepo;
    }

    @Override
    public Ratings giveRating(RatingsRequestDto ratingsRequestDto) {

        String tlId = ratingsRequestDto.getRated_by_id();
        String empId = ratingsRequestDto.getEmployee_id();

        if(tlId == null || empId == null){
            throw new ResourceNotFoundException("Both TL ID and Employee ID should be provided!");
        }

        Employees tl = employeesRepo.findByEmployeeIdIgnoreCase(tlId)
                .orElseThrow(() -> new ResourceNotFoundException("TL not found with ID: " + tlId));

        if(tl.getRole() == null || !"R102".equalsIgnoreCase(tl.getRole().getRoleId())){
            throw new APIException("Only TL can give this rating.");
        }

        Employees emp = employeesRepo.findByEmployeeIdIgnoreCase(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + empId));

        if(emp.getRole() == null || !"R103".equalsIgnoreCase(emp.getRole().getRoleId())){
            throw new APIException("Only Employee can receive this rating.");
        }

        Ratings rating = new Ratings();
       // rating.setRatingRole(ratingsRequestDto.getRating_role());
        rating.setRatingValue(ratingsRequestDto.getRating_value());

        return rating;
    }
}
