package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.Factory.RatingFactory;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.entity.*;
import com.example.RatingsApp.entity.enums.CycleStatus;
import com.example.RatingsApp.entity.enums.RatingDescription;
import com.example.RatingsApp.entity.enums.RatingTypes;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.*;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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

    private Ratings buildRatingStrategy(RatingsRequestDto ratingsRequestDto, RatingTypes role) {

        Employees employee = employeesRepo.findByEmployeeId(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Employees ratedBy = employeesRepo.findByEmployeeId(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("RatedBy employee not found"));

        RatingsCycle cycle = ratingsCycleRepo.findByStatus(CycleStatus.ACTIVE)
                .orElseThrow(() -> new APIException("No ACTIVE ratings cycle available."));

        RatingStrategy strategy = ratingFactory.getStrategy(String.valueOf(role));
        Ratings rating = strategy.giveRating(ratingsRequestDto);

        rating.setEmployee(employee);
        rating.setRatedBy(ratedBy);
        rating.setRatingTypes(role);
        rating.setRatingsCycle(cycle);
        rating.setRatingDescription(ratingDescription(rating));

        return rating;
    }

//    @Override
//    public List<RatingsResponseDto> getPendingRatings(String employeeId) {
//
//        Employees employee = employeesRepo.findByEmployeeId(employeeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
//
//        List<Ratings> pending = ratingsRepo.
//
//        return pending.stream().map(RatingsResponseDto::new).toList();
//    }

    @Override
    public List<RatingsResponseDto> getBroadcastedRatings() {

        List<Ratings> broadcastedRatings = ratingsRepo.findByRatingStatus(RatingStatus.BROADCASTED);

        return broadcastedRatings.stream().map(RatingsResponseDto::new).toList();
    }

    @Override
    public List<RatingsResponseDto> getRatingsByTeam(String teamId) {

        Teams team = teamsRepo.findByTeamId(teamId)
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        List<Employees> employees = employeesRepo.findByTeam(team);

        List<Ratings> ratings = ratingsRepo.findByEmployeeIn(employees);

        return ratings.stream().map(RatingsResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<RatingsResponseDto> getSelfRatings() {

        List<Ratings> ratings = ratingsRepo.findByRatingTypes(RatingTypes.SELF);

        return ratings.stream().map(RatingsResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto) {

        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Employees loggedInEmployee = employeesRepo.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));

        ratingsRequestDto.setRated_by_id(loggedInEmployee.getEmployeeId());

        RatingTypes role = ratingsRequestDto.getRating_types();

        RatingsCycle cycle = ratingsCycleRepo.findByStatus(CycleStatus.ACTIVE)
                .orElseThrow(() -> new APIException("No ACTIVE ratings cycle found"));

        if (loggedInEmployee.getTeam() == null || loggedInEmployee.getTeam().getTeamId() == null) {
            throw new APIException("Employee not yet assigned to a team!");
        }

        Ratings ratings;

        switch (role) {
            case SELF: {
                Ratings existing = ratingsRepo.findExistingRating(loggedInEmployee.getEmployeeId(), loggedInEmployee.getEmployeeId(), RatingTypes.SELF, cycle.getCycleId()).orElse(null);
                if (existing != null && existing.getRatingStatus() == RatingStatus.BROADCASTED) {
                    return new RatingsResponseDto(existing);
                }
                if (existing != null) {
                    existing.setRatingValue(ratingsRequestDto.getRating_value());
                    existing.setRatingDescription(ratingDescription(existing));
                    existing.setRatingStatus(RatingStatus.SUBMITTED);
                    ratingsRepo.save(existing);
                    return new RatingsResponseDto(existing);
                }
                ratings = buildRatingStrategy(ratingsRequestDto, RatingTypes.SELF);
                ratings.setRatingStatus(RatingStatus.SUBMITTED);
                break;
            }
            case TL_TO_TM: {
                Ratings existing = ratingsRepo.findExistingRating(loggedInEmployee.getEmployeeId(), ratingsRequestDto.getEmployee_id(), RatingTypes.TL_TO_TM, cycle.getCycleId()).orElse(null);
                if (existing != null && existing.getRatingStatus() == RatingStatus.BROADCASTED) {
                    return new RatingsResponseDto(existing);
                }
                if (existing != null) {
                    existing.setRatingValue(ratingsRequestDto.getRating_value());
                    existing.setRatingDescription(ratingDescription(existing));
                    existing.setRatingStatus(RatingStatus.SUBMITTED);
                    ratingsRepo.save(existing);
                    return new RatingsResponseDto(existing);
                }
                ratings = buildRatingStrategy(ratingsRequestDto, RatingTypes.TL_TO_TM);
                ratings.setRatingStatus(RatingStatus.SUBMITTED_BY_TL);
                break;
            }
            case PM_TO_TL: {
                Ratings existing = ratingsRepo.findExistingRating(loggedInEmployee.getEmployeeId(), ratingsRequestDto.getEmployee_id(), RatingTypes.PM_TO_TL, cycle.getCycleId()).orElse(null);
                if (existing != null && existing.getRatingStatus() == RatingStatus.BROADCASTED) {
                    return new RatingsResponseDto(existing);
                }
                if (existing != null) {
                    existing.setRatingValue(ratingsRequestDto.getRating_value());
                    existing.setRatingDescription(ratingDescription(existing));
                    existing.setRatingStatus(RatingStatus.SUBMITTED);
                    ratingsRepo.save(existing);
                    return new RatingsResponseDto(existing);
                }
                ratings = buildRatingStrategy(ratingsRequestDto, RatingTypes.PM_TO_TL);
                ratings.setRatingStatus(RatingStatus.SUBMITTED_BY_PM);
                break;
            }
            case PM_TO_TM: {
                Ratings existing = ratingsRepo.findExistingRating(loggedInEmployee.getEmployeeId(), ratingsRequestDto.getEmployee_id(), RatingTypes.PM_TO_TM, cycle.getCycleId()).orElse(null);
                if (existing != null && existing.getRatingStatus() == RatingStatus.BROADCASTED) {
                    return new RatingsResponseDto(existing);
                }
                if (existing != null) {
                    existing.setRatingValue(ratingsRequestDto.getRating_value());
                    existing.setRatingDescription(ratingDescription(existing));
                    existing.setRatingStatus(RatingStatus.SUBMITTED);
                    ratingsRepo.save(existing);
                    return new RatingsResponseDto(existing);
                }
                ratings = buildRatingStrategy(ratingsRequestDto, RatingTypes.PM_TO_TM);
                ratings.setRatedBy(loggedInEmployee);
                ratings.setRatingStatus(RatingStatus.SUBMITTED_BY_PM);
                break;
            }
            default:
                throw new APIException("Invalid Rating Role provided.");
        }
        Ratings saved = ratingsRepo.save(ratings);
        return new RatingsResponseDto(saved);
    }

    @Override
    public List<RatingsResponseDto> getAllRatings() {
        List<Ratings> ratings = ratingsRepo.findAll(Sort.by(Sort.Direction.ASC, "ratingId"));
        return ratings.stream().map(RatingsResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public RatingsResponseDto getRatingById(String ratingId) {
        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));
        return new RatingsResponseDto(rating);
    }

    private Ratings getExistingRating(String employeeId, String ratedById, RatingTypes role) {

        RatingsCycle cycle = ratingsCycleRepo.findByStatus(CycleStatus.ACTIVE)
                .orElseThrow(() -> new APIException("No ACTIVE cycle found"));

        return ratingsRepo.findExistingRating(employeeId, ratedById, role, cycle.getCycleId()).orElse(null);
    }

    @Override
    public RatingsResponseDto updateRating(String ratingId, RatingsRequestDto ratingsRequestDto) {
        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));

        if (rating.getRatingStatus() == RatingStatus.BROADCASTED) {
            throw new APIException("Rating is Broadcasted and cannot be modified.");
        }

//        Employees employee = employeesRepo.findByEmployeeId(ratingsRequestDto.getEmployee_id())
//                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
//        Employees ratedBy = employeesRepo.findByEmployeeId(ratingsRequestDto.getRated_by_id())
//                .orElseThrow(() -> new ResourceNotFoundException("Rated By Employee not found"));

//        RatingsCycle cycle = ratingsCycleRepo.findByStatus(CycleStatus.ACTIVE)
//                .orElseThrow(() -> new APIException("No ACTIVE ratings cycle available."));

        rating.setRatingStatus(RatingStatus.SUBMITTED);
//        rating.setRatingsCycle(cycle);
        rating.setRatingValue(ratingsRequestDto.getRating_value());

        RatingDescription ratingDescription = ratingDescription(rating);
        rating.setRatingDescription(ratingDescription);
        ratingsRepo.save(rating);

        return new RatingsResponseDto(rating);
    }

    @Override
    public RatingsResponseDto deleteRating(String ratingId) {
        Ratings rating = ratingsRepo.findByRatingId(ratingId)
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
    public RatingsResponseDto broadcastRating(String ratingId, RatingsRequestDto ratingsRequestDto) {

        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));

        Employees broadcaster = employeesRepo.findByEmployeeId(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("Broadcaster not found"));

        String broadcasterId = broadcaster.getRole().getRoleId();

        rating.setRatingStatus(RatingStatus.BROADCASTED);
        ratingsRepo.save(rating);
        return new RatingsResponseDto(rating);
    }

    @Override
    public List<RatingsResponseDto> getReceivedRatings(String employeeId) {
        Employees employee = employeesRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        List<Ratings> ratingsList = ratingsRepo.findByEmployee(employee);

        return ratingsList.stream().map(RatingsResponseDto::new).toList();
    }

    @Override
    public List<RatingsResponseDto> getGivenRatings(String ratedById) {

        Employees ratedBy = employeesRepo.findByEmployeeId(ratedById)
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
    public OptionalDouble getAverageByTeam(String teamId) {

        Teams team = teamsRepo.findByTeamId(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        List<Employees> employees = employeesRepo.findByTeam(team);

        List<Ratings> ratingsList = employees.stream()
                .flatMap(emp -> ratingsRepo.findByEmployee(emp).stream())
                .toList();

        return ratingsList.stream()
                .mapToInt(Ratings::getRatingValue)
                .average();
    }

    private RatingTypes deriveRatingRole(Employees ratedBy, Employees employee) {

        String ratedByRole = ratedBy.getRole().getRoleId();
        String employeeRole = employee.getRole().getRoleId();

        String PM = rolesRepo.findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("PM role not found"))
                .getRoleId();

        String TL = rolesRepo.findById(3L)
                .orElseThrow(() -> new ResourceNotFoundException("TL role not found"))
                .getRoleId();

        String TM = rolesRepo.findById(4L)
                .orElseThrow(() -> new ResourceNotFoundException("Employee role not found"))
                .getRoleId();

        if (ratedBy.getEmployeeId().equals(employee.getEmployeeId())) {
            return RatingTypes.SELF;
        }
        if (ratedByRole.equals(PM) && employeeRole.equals(TL)) {
            return RatingTypes.PM_TO_TL;
        }
        if (ratedByRole.equals(PM) && employeeRole.equals(TM)) {
            return RatingTypes.PM_TO_TM;
        }
        if (ratedByRole.equals(TL) && employeeRole.equals(TM)) {
            return RatingTypes.TL_TO_TM;
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
            return RatingDescription.EXCEEDING_EXPECTATION;
        }
        else if (ratingValue >= 5 && ratingValue <= 6) {
            return RatingDescription.MEET_EXPECTATION;
        }
        else if (ratingValue >= 3 && ratingValue <= 4) {
            return RatingDescription.NEEDS_IMPROVEMENT;
        }
        else if (ratingValue >= 1 && ratingValue <= 2) {
            return RatingDescription.BELOW_EXPECTATION;
        }
        else {
            throw new IllegalArgumentException("Invalid rating value: " + ratingValue);
        }
    }
}
