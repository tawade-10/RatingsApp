package com.example.RatingsApp.strategyimpl.pmratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.stereotype.Component;

@Component("PM_TO_TTL")
public class PmToTTLRating implements RatingStrategy {

    private final EmployeesRepo employeesRepo;

    public PmToTTLRating(EmployeesRepo employeesRepo) {
        this.employeesRepo = employeesRepo;
    }

    @Override
    public Ratings giveRating(RatingsRequestDto ratingsRequestDto) {
        Long pmId = ratingsRequestDto.getRated_by_id();
        Long employeeId = ratingsRequestDto.getEmployee_id();

        if(pmId == null || employeeId == null){
            throw new APIException("Both PM ID and Employee ID must be provided.");
        }

        Employees pm = employeesRepo.findById(pmId)
                .orElseThrow(() -> new ResourceNotFoundException("PM not found with ID: " + pmId));

        if (pm.getRole() == null || pm.getRole().getRoleId() != 1L) {
            throw new APIException("Only PM can give this rating.");
        }

        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        if(employee.getRole() == null || employee.getRole().getRoleId() != 2L){
            throw new APIException("Only TTL can receive this rating.");
        }

        if (pm.getTeam() == null || employee.getTeam() == null) {
            throw new APIException("Both PM and Employee must be assigned to a team.");
        }

        if(!pm.getTeam().getTeamId().equals(employee.getTeam().getTeamId())){
            throw new APIException("PM can only rate members from their own team.");
        }

        Ratings rating = new Ratings();

        rating.setRatingValue(ratingsRequestDto.getRating_value());
        rating.setRatingRole(ratingsRequestDto.getRating_role());
        rating.setRatingStatus(ratingsRequestDto.getRating_status());

        return rating;
    }
}
