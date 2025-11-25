package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.Factory.RatingFactory;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
import com.example.RatingsApp.entity.*;
import com.example.RatingsApp.entity.enums.CycleStatus;
import com.example.RatingsApp.entity.enums.RatingDescription;
import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.*;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class RatingsServiceImpl implements RatingsService {

    private final EmployeesRepo employeesRepo;

    private final RatingsRepo ratingsRepo;

    private final RatingFactory ratingFactory;

    private final TeamsRepo teamsRepo;

    private final RatingsCycleRepo ratingsCycleRepo;

    private final RolesRepo rolesRepo;

    public RatingsServiceImpl(EmployeesRepo employeesRepo, RatingsRepo ratingsRepo, RatingFactory ratingFactory, TeamsRepo teamsRepo, RatingsCycleRepo ratingsCycleRepo, RolesRepo rolesRepo) {
        this.employeesRepo = employeesRepo;
        this.ratingsRepo = ratingsRepo;
        this.ratingFactory = ratingFactory;
        this.teamsRepo = teamsRepo;
        this.ratingsCycleRepo = ratingsCycleRepo;
        this.rolesRepo = rolesRepo;
    }

    @Override
    public RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto) {

        Employees employee = employeesRepo.findById(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Employees ratedBy = employeesRepo.findById(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("RatedBy employee not found"));

        RatingsCycle cycle = ratingsCycleRepo.findByCycleNameIgnoreCase(ratingsRequestDto.getRatings_cycle())
                .orElseThrow(() -> new ResourceNotFoundException("Ratings Cycle not found"));

        if (cycle.getStatus() != CycleStatus.ACTIVE) {
            throw new APIException("Ratings can only be created when cycle is ACTIVE.");
        }

//        Optional<Ratings> existingRatings = ratingsRepo.f
//        if (existingRatings.isPresent()) {
//            return new RolesResponseDto(existingRatings.get());
//        }

        RatingRoles derivedRole = deriveRatingRole(ratedBy, employee);

        RatingStrategy strategy = ratingFactory.getStrategy(String.valueOf(derivedRole));

        Ratings rating = strategy.giveRating(ratingsRequestDto);
        RatingDescription ratingDescription = ratingDescription(rating);

        rating.setEmployee(employee);
        rating.setRatedBy(ratedBy);
        rating.setRatingRole(derivedRole);
        rating.setRatingsCycle(cycle);
        rating.setRatingDescription(ratingDescription);

        Ratings savedRating = ratingsRepo.save(rating);

        return new RatingsResponseDto(savedRating);
    }

    @Override
    public List<RatingsResponseDto> getAllRatings() {
        List<Ratings> ratings = ratingsRepo.findAll(Sort.by(Sort.Direction.ASC, "ratingId"));
        return ratings.stream().map(RatingsResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public RatingsResponseDto getRatingById(Long ratingId) {
        Ratings rating = ratingsRepo.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));
        return new RatingsResponseDto(rating);
    }

    @Override
    public RatingsResponseDto updateRating(Long ratingId, RatingsRequestDto ratingsRequestDto) {
        Ratings rating = ratingsRepo.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));

        if (rating.getRatingStatus() == RatingStatus.BROADCASTED) {
            throw new APIException("Rating is Broadcasted and cannot be modified.");
        }

        Employees employee = employeesRepo.findById(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Employees ratedBy = employeesRepo.findById(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("Rated By Employee not found"));

        RatingsCycle cycle = ratingsCycleRepo.findByCycleNameIgnoreCase(ratingsRequestDto.getRatings_cycle())
                .orElseThrow(() -> new ResourceNotFoundException("Ratings Cycle not found"));

        rating.setRatingStatus(RatingStatus.SUBMITTED);
        rating.setRatingsCycle(cycle);
        rating.setRatingValue(ratingsRequestDto.getRating_value());

        RatingDescription ratingDescription = ratingDescription(rating);
        rating.setRatingDescription(ratingDescription);
        ratingsRepo.save(rating);

        return new RatingsResponseDto(rating);
    }

    @Override
    public RatingsResponseDto deleteRating(Long ratingId) {
        Ratings rating = ratingsRepo.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));
        ratingsRepo.delete(rating);
        return null;
    }

    @Override
    public List<RatingsResponseDto> getRatingsByCycles(String ratingsCycle) {
        List<Ratings> ratings = ratingsRepo.findByRatingsCycle_CycleNameIgnoreCase(ratingsCycle);
        return ratings.stream().map(RatingsResponseDto::new).collect(Collectors.toList());
    }

//    @Override
//    public RatingsResponseDto approveRating(String ratingId, RatingsRequestDto ratingsRequestDto) {
//
//        Ratings rating = ratingsRepo.findByRatingId(ratingId)
//                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));
//
//        if (rating.getRatingStatus() == RatingStatus.APPROVED_BY_PM || rating.getRatingStatus() == RatingStatus.BROADCASTED) {
//            throw new APIException("Rating cannot be modified after final approval/broadcast.");
//        }
//
//        if (rating.getRatingStatus() == null || !(rating.getRatingStatus() == RatingStatus.SUBMITTED ||
//                        rating.getRatingStatus() == RatingStatus.APPROVED_BY_TL ||
//                        rating.getRatingStatus() == RatingStatus.APPROVED_BY_TTL)) {
//            throw new IllegalArgumentException("Only SUBMITTED / APPROVED_BY_TL / APPROVED_BY_TTL ratings can be approved.");
//        }
//
//        Employees ratedEmployee = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getEmployee_id())
//                .orElseThrow(() -> new ResourceNotFoundException("Rated Employee not found"));
//
//        Employees approver = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getRated_by_id())
//                .orElseThrow(() -> new ResourceNotFoundException("Approver not found"));
//
//        String ratedRole = ratedEmployee.getRole().getRoleId();
//        String approverRole = approver.getRole().getRoleId();
//
//        if (approver.getTeam() == null || ratedEmployee.getTeam() == null || !approver.getTeam().getTeamId().equals(ratedEmployee.getTeam().getTeamId())) {
//            throw new IllegalArgumentException("Approver and Rated employee must belong to same team.");
//        }
//
//        if ("R104".equalsIgnoreCase(ratedRole)) {
//            if ("R103".equalsIgnoreCase(approverRole)) {
//                rating.setRatingStatus(RatingStatus.APPROVED_BY_TL);
//            }
//            else if ("R102".equalsIgnoreCase(approverRole)) {
//                if (rating.getRatingStatus() != RatingStatus.APPROVED_BY_TL){
//                    throw new IllegalArgumentException("Individual rating must be TL-approved before TTL approval.");
//                }else {
//                    rating.setRatingStatus(RatingStatus.APPROVED_BY_TTL);
//                }
//            }
//            else if ("R101".equalsIgnoreCase(approverRole)) {
//                if (rating.getRatingStatus() != RatingStatus.APPROVED_BY_TTL){
//                    throw new IllegalArgumentException("Individual rating must be TTL-approved before PM approval.");
//                }else{
//                    rating.setRatingStatus(RatingStatus.APPROVED_BY_PM);
//                }
//            }
//            else {
//                throw new IllegalArgumentException("Invalid approver for Individual rating.");
//            }
//        }
//        else if ("R103".equalsIgnoreCase(ratedRole)) {
//            if ("R102".equalsIgnoreCase(approverRole)) {
//                rating.setRatingStatus(RatingStatus.APPROVED_BY_TTL);
//            }
//            else if ("R101".equalsIgnoreCase(approverRole)) {
//                if (rating.getRatingStatus() != RatingStatus.APPROVED_BY_TTL){
//                    throw new IllegalArgumentException("TL rating must be TTL-approved before PM approval.");
//                }else{
//                    rating.setRatingStatus(RatingStatus.APPROVED_BY_PM);
//                }
//            }
//            else {
//                throw new IllegalArgumentException("Invalid approver for TL rating.");
//            }
//        }
//        else if ("R102".equalsIgnoreCase(ratedRole)) {
//            if ("R101".equalsIgnoreCase(approverRole)) {
//                rating.setRatingStatus(RatingStatus.APPROVED_BY_PM);
//            }
//            else {
//                throw new IllegalArgumentException("Only PM can approve TTL ratings.");
//            }
//        }
//        ratingsRepo.save(rating);
//        return new RatingsResponseDto(rating);
//    }

    @Override
    public RatingsResponseDto broadcastRating(Long ratingId, RatingsRequestDto ratingsRequestDto) {

        Ratings rating = ratingsRepo.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));

        Employees broadcaster = employeesRepo.findById(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("Broadcaster not found"));

        Long broadcasterId = broadcaster.getRole().getRoleId();

        rating.setRatingStatus(RatingStatus.BROADCASTED);
        ratingsRepo.save(rating);
        return new RatingsResponseDto(rating);
    }

    @Override
    public List<RatingsResponseDto> getReceivedRatings(Long employeeId) {
        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        List<Ratings> ratingsList = ratingsRepo.findByEmployee(employee);

        return ratingsList.stream().map(RatingsResponseDto::new).toList();
    }

    @Override
    public List<RatingsResponseDto> getGivenRatings(Long ratedById) {

        Employees ratedBy = employeesRepo.findById(ratedById)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + ratedById));

        List<Ratings> ratingsList = ratingsRepo.findByRatedBy(ratedBy);

        return ratingsList.stream().map(RatingsResponseDto::new).toList();
    }

    @Override
    public OptionalDouble getAverageByCycle(String cycleName) {

        RatingsCycle cycle = ratingsCycleRepo.findByCycleNameIgnoreCase(cycleName)
                .orElseThrow(() -> new ResourceNotFoundException("Ratings Cycle not found with name: " + cycleName));

        List<Ratings> ratingsList = ratingsRepo.findByRatingsCycle_CycleNameIgnoreCase(cycleName);

        return ratingsList.stream().mapToInt(Ratings::getRatingValue).average();
    }

    @Override
    public OptionalDouble getAverageByTeam(Long teamId) {

        Teams team = teamsRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        List<Employees> employees = employeesRepo.findByTeam(team);

        List<Ratings> ratingsList = employees.stream()
                .flatMap(emp -> ratingsRepo.findByEmployee(emp).stream())
                .toList();

        return ratingsList.stream()
                .mapToInt(Ratings::getRatingValue)
                .average();
    }


    private RatingRoles deriveRatingRole(Employees ratedBy, Employees employee) {

        Long ratedByRole = ratedBy.getRole().getRoleId();
        Long employeeRole = employee.getRole().getRoleId();

        Long PM = rolesRepo.findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("PM role not found"))
                .getRoleId();

//        Long TTL = rolesRepo.findByRoleNameIgnoreCase("TTL")
//                .orElseThrow(() -> new ResourceNotFoundException("TTL role not found"))
//                .getRoleId();

        Long TL = rolesRepo.findById(3L)
                .orElseThrow(() -> new ResourceNotFoundException("TL role not found"))
                .getRoleId();

        Long EMP = rolesRepo.findById(4L)
                .orElseThrow(() -> new ResourceNotFoundException("Employee role not found"))
                .getRoleId();

        if (ratedBy.getEmployeeId().equals(employee.getEmployeeId())) {
            return RatingRoles.SELF;
        }
        if (ratedByRole.equals(PM) && employeeRole.equals(TL)) {
            return RatingRoles.PM_TO_TL;
        }
        if (ratedByRole.equals(PM) && employeeRole.equals(EMP)) {
            return RatingRoles.PM_TO_EMPLOYEE;
        }
        if (ratedByRole.equals(TL) && employeeRole.equals(EMP)) {
            return RatingRoles.TL_TO_EMPLOYEE;
        }
        throw new APIException(
                "Rating combination is Invalid between " +
                        ratedBy.getName() + " (" + ratedByRole + ") and " +
                        employee.getName() + " (" + employeeRole + ")"
        );
    }


    private RatingDescription ratingDescription(Ratings ratings) {
        int ratingValue = ratings.getRatingValue();

        if (ratingValue >= 9 && ratingValue <= 10) {
            return RatingDescription.OUTSTANDING;
        }
        else if (ratingValue >= 7 && ratingValue <= 8) {
            return RatingDescription.GOOD;
        }
        else if (ratingValue >= 5 && ratingValue <= 6) {
            return RatingDescription.AVERAGE;
        }
        else if (ratingValue >= 3 && ratingValue <= 4) {
            return RatingDescription.BELOW_AVERAGE;
        }
        else if (ratingValue >= 1 && ratingValue <= 2) {
            return RatingDescription.POOR;
        }
        else {
            throw new IllegalArgumentException("Invalid rating value: " + ratingValue);
        }
    }
}
