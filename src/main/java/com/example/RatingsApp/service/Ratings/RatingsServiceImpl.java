package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.Factory.RatingFactory;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.entity.enums.CycleStatus;
import com.example.RatingsApp.entity.enums.RatingDescription;
import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RatingsCycleRepo;
import com.example.RatingsApp.repository.RatingsRepo;
import com.example.RatingsApp.repository.TeamsRepo;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public RatingsServiceImpl(EmployeesRepo employeesRepo, RatingsRepo ratingsRepo, RatingFactory ratingFactory, TeamsRepo teamsRepo, RatingsCycleRepo ratingsCycleRepo) {
        this.employeesRepo = employeesRepo;
        this.ratingsRepo = ratingsRepo;
        this.ratingFactory = ratingFactory;
        this.teamsRepo = teamsRepo;
        this.ratingsCycleRepo = ratingsCycleRepo;
    }

    @Override
    public RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto) {

        // Fetch Employees
        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Employees ratedBy = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("RatedBy employee not found"));

//        if("R101".equalsIgnoreCase(ratedByRole) && "R103".equalsIgnoreCase(employeeRole)){
//            throw new IllegalArgumentException("PM cannot rate Individual");
//        }

        if (ratingsRequestDto.getRatingId() == null || ratingsRequestDto.getRatingId().isBlank())
            throw new IllegalArgumentException("Rating Id cannot be null or empty");

        if (ratingsRepo.findByRatingId(ratingsRequestDto.getRatingId()).isPresent()) {
            throw new APIException("Rating with ID : '" + ratingsRequestDto.getRatingId() + "' already exists!");
        }

        // Fetch Cycle
        RatingsCycle cycle = ratingsCycleRepo.findByCycleNameIgnoreCase(ratingsRequestDto.getRatings_cycle())
                .orElseThrow(() -> new ResourceNotFoundException("Ratings Cycle not found"));

        if (cycle.getStatus() != CycleStatus.ACTIVE) {
            throw new APIException("Ratings can only be created when cycle is ACTIVE.");
        }

        // Derive Role
        RatingRoles derivedRole = deriveRatingRole(ratedBy, employee);

        // Fetching Rating Strategy
        RatingStrategy strategy = ratingFactory.getStrategy(String.valueOf(derivedRole));

        // Fetching Strategy Logic
        Ratings rating = strategy.giveRating(ratingsRequestDto);

        RatingDescription ratingDescription = ratingDescription(rating);

        // Set remaining fields
        rating.setRatingId(ratingsRequestDto.getRatingId());
        rating.setEmployee(employee);
        rating.setRatedBy(ratedBy);
        rating.setRatingRole(derivedRole);
        rating.setRatingsCycle(cycle);
        rating.setRatingDescription(ratingDescription);
        rating.setRatingStatus(RatingStatus.SUBMITTED);

        Ratings savedRating = ratingsRepo.save(rating);

        return new RatingsResponseDto(savedRating);
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

    @Override
    public RatingsResponseDto updateRating(String ratingId, RatingsRequestDto ratingsRequestDto) {
        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));

        if (rating.getRatingStatus() == RatingStatus.APPROVED_BY_PM || rating.getRatingStatus() == RatingStatus.BROADCASTED) {
            throw new APIException("Rating is locked and cannot be modified after approval/broadcast.");
        }

        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Employees ratedBy = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getRated_by_id())
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

    @Override
    public RatingsResponseDto approveRating(String ratingId, RatingsRequestDto ratingsRequestDto) {

        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));

        if (rating.getRatingStatus() == RatingStatus.APPROVED_BY_PM || rating.getRatingStatus() == RatingStatus.BROADCASTED) {
            throw new APIException("Rating cannot be modified after final approval/broadcast.");
        }

        if (rating.getRatingStatus() == null || !(rating.getRatingStatus() == RatingStatus.SUBMITTED ||
                        rating.getRatingStatus() == RatingStatus.APPROVED_BY_TL ||
                        rating.getRatingStatus() == RatingStatus.APPROVED_BY_TTL)) {
            throw new IllegalArgumentException("Only SUBMITTED / APPROVED_BY_TL / APPROVED_BY_TTL ratings can be approved.");
        }

        Employees ratedEmployee = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Rated Employee not found"));

        Employees approver = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("Approver not found"));

        String ratedRole = ratedEmployee.getRole().getRoleId();
        String approverRole = approver.getRole().getRoleId();

        if (approver.getTeam() == null || ratedEmployee.getTeam() == null || !approver.getTeam().getTeamId().equals(ratedEmployee.getTeam().getTeamId())) {
            throw new IllegalArgumentException("Approver and Rated employee must belong to same team.");
        }

        if ("R104".equalsIgnoreCase(ratedRole)) {
            if ("R103".equalsIgnoreCase(approverRole)) {
                rating.setRatingStatus(RatingStatus.APPROVED_BY_TL);
            }
            else if ("R102".equalsIgnoreCase(approverRole)) {
                if (rating.getRatingStatus() != RatingStatus.APPROVED_BY_TL){
                    throw new IllegalArgumentException("Individual rating must be TL-approved before TTL approval.");
                }else {
                    rating.setRatingStatus(RatingStatus.APPROVED_BY_TTL);
                }
            }
            else if ("R101".equalsIgnoreCase(approverRole)) {
                if (rating.getRatingStatus() != RatingStatus.APPROVED_BY_TTL){
                    throw new IllegalArgumentException("Individual rating must be TTL-approved before PM approval.");
                }else{
                    rating.setRatingStatus(RatingStatus.APPROVED_BY_PM);
                }
            }
            else {
                throw new IllegalArgumentException("Invalid approver for Individual rating.");
            }
        }
        else if ("R103".equalsIgnoreCase(ratedRole)) {
            if ("R102".equalsIgnoreCase(approverRole)) {
                rating.setRatingStatus(RatingStatus.APPROVED_BY_TTL);
            }
            else if ("R101".equalsIgnoreCase(approverRole)) {
                if (rating.getRatingStatus() != RatingStatus.APPROVED_BY_TTL){
                    throw new IllegalArgumentException("TL rating must be TTL-approved before PM approval.");
                }else{
                    rating.setRatingStatus(RatingStatus.APPROVED_BY_PM);
                }
            }
            else {
                throw new IllegalArgumentException("Invalid approver for TL rating.");
            }
        }
        else if ("R102".equalsIgnoreCase(ratedRole)) {
            if ("R101".equalsIgnoreCase(approverRole)) {
                rating.setRatingStatus(RatingStatus.APPROVED_BY_PM);
            }
            else {
                throw new IllegalArgumentException("Only PM can approve TTL ratings.");
            }
        }
        ratingsRepo.save(rating);
        return new RatingsResponseDto(rating);
    }

    @Override
    public RatingsResponseDto broadcastRating(String ratingId, RatingsRequestDto ratingsRequestDto) {

        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));

        Employees broadcaster = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("Broadcaster not found"));

        Employees approvedEmployee = rating.getEmployee();

        if (!broadcaster.getTeam().getTeamId().equals(approvedEmployee.getTeam().getTeamId())) {
            throw new IllegalArgumentException("Broadcaster and approved employee must belong to the same team");
        }
        if (!"R101".equalsIgnoreCase(broadcaster.getRole().getRoleId())) {
            throw new IllegalArgumentException("Only PM can broadcast ratings");
        }

        rating.setRatingStatus(RatingStatus.BROADCASTED);
        ratingsRepo.save(rating);

        return new RatingsResponseDto(rating);
    }
    
    @Override
    public List<RatingsResponseDto> getReceivedRatings(String employeeId) {
        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        List<Ratings> ratingsList = ratingsRepo.findByEmployee(employee);

        return ratingsList.stream().map(RatingsResponseDto::new).toList();
    }

    @Override
    public List<RatingsResponseDto> getGivenRatings(String ratedById) {

        Employees ratedBy = employeesRepo.findByEmployeeIdIgnoreCase(ratedById)
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

        Teams team = teamsRepo.findByTeamIdIgnoreCase(teamId)
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
        String ratedByRole = ratedBy.getRole().getRoleId();
        String employeeRole = employee.getRole().getRoleId();

        if (ratedBy.getEmployeeId().equalsIgnoreCase(employee.getEmployeeId())) {
            return RatingRoles.SELF;
        }
        if ("R101".equalsIgnoreCase(ratedByRole) && "R102".equalsIgnoreCase(employeeRole))
            return RatingRoles.PM_TO_TTL;
        if ("R102".equalsIgnoreCase(ratedByRole) && "R103".equalsIgnoreCase(employeeRole))
            return RatingRoles.TTL_TO_TL;
        if ("R103".equalsIgnoreCase(ratedByRole) && "R104".equalsIgnoreCase(employeeRole))
            return RatingRoles.TL_TO_EMPLOYEE;
        throw new APIException("Rating combination is Invalid between " +
                ratedBy.getName() + " (" + ratedByRole + ") and " +
                employee.getName() + " (" + employeeRole + ")");
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
