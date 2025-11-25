package com.example.RatingsApp.strategyimpl.tlratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.stereotype.Component;

@Component("TL_TO_EMPLOYEE")
public class TLToIndividualRating implements RatingStrategy {

    private final EmployeesRepo employeesRepo;

    private final RolesRepo rolesRepo;

    public TLToIndividualRating(EmployeesRepo employeesRepo, RolesRepo rolesRepo) {
        this.employeesRepo = employeesRepo;
        this.rolesRepo = rolesRepo;
    }

    @Override
    public Ratings giveRating(RatingsRequestDto ratingsRequestDto) {

        Long ratedById = ratingsRequestDto.getRated_by_id();
        Long employeeId = ratingsRequestDto.getEmployee_id();

        if (ratedById == null || employeeId == null) {
            throw new APIException("Both RatedBy ID and Employee ID must be provided.");
        }

        Employees ratedBy = employeesRepo.findById(ratedById)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + ratedById));

        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Long TL = rolesRepo.findById(3L)
                .orElseThrow(() -> new ResourceNotFoundException("TL role not found"))
                .getRoleId();

        Long INDIVIDUAL = rolesRepo.findById(4L)
                .orElseThrow(() -> new ResourceNotFoundException("INDIVIDUAL role not found"))
                .getRoleId();

        if (!ratedBy.getRole().getRoleId().equals(TL)) {
            throw new APIException("Only TL can give this rating.");
        }

        if (!employee.getRole().getRoleId().equals(INDIVIDUAL)) {
            throw new APIException("Only INDIVIDUAL can receive this rating.");
        }

        if (!ratedBy.getTeam().getTeamId().equals(employee.getTeam().getTeamId())) {
            throw new APIException("TL can only rate INDIVIDUAL's of their own team.");
        }

        Ratings ratings = new Ratings();

        ratings.setRatingStatus(RatingStatus.SUBMITTED_BY_TL);
        ratings.setRatingValue(ratingsRequestDto.getRating_value());

        return ratings;
    }
}
