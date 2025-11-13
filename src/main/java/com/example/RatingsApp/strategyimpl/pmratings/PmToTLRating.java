package com.example.RatingsApp.strategyimpl.pmratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.stereotype.Component;

@Component("PM_TO_TL")
public class PmToTLRating implements RatingStrategy {

    private final EmployeesRepo employeesRepo;

    public PmToTLRating(EmployeesRepo employeesRepo) {
        this.employeesRepo = employeesRepo;
    }

    @Override
    public Ratings giveRating(RatingsRequestDto ratingsRequestDto) {
        String pmId = ratingsRequestDto.getRated_by_id();
        String employeeId = ratingsRequestDto.getEmployee_id();

        if(pmId == null || employeeId == null){
            throw new APIException("Both PM ID and Employee ID must be provided.");
        }

        Employees pm = employeesRepo.findByEmployeeIdIgnoreCase(pmId)
                .orElseThrow(() -> new ResourceNotFoundException("PM not found with ID: " + pmId));

        if (pm.getRole() == null || !"R101".equalsIgnoreCase(pm.getRole().getRoleId())) {
            throw new APIException("Only PM can give this rating.");
        }

        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        if(employee.getRole() == null || !"R102".equalsIgnoreCase(employee.getRole().getRoleId())){
            throw new APIException("Only TL can receive this rating.");
        }

        if(!pm.getTeam().getTeamId().equals(employee.getTeam().getTeamId())){
            throw new APIException("PM can only rate TL's of their own team");
        }

        Ratings rating = new Ratings();
        rating.setRatingValue(ratingsRequestDto.getRating_value());
        //rating.setRatingRole(ratingsRequestDto.getRating_role());

        return rating;
    }
}
