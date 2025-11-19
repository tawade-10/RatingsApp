package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.Factory.RatingFactory;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.enums.CycleStatus;
import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
// import com.example.RatingsApp.repository.RatingsCycleRepo;
import com.example.RatingsApp.repository.RatingsCycleRepo;
import com.example.RatingsApp.repository.RatingsRepo;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingsServiceImpl implements RatingsService {

    private final EmployeesRepo employeesRepo;

    private final RatingsRepo ratingsRepo;

    private final RatingFactory ratingFactory;

    private final RatingsCycleRepo ratingsCycleRepo;

    public RatingsServiceImpl(EmployeesRepo employeesRepo, RatingsRepo ratingsRepo, RatingFactory ratingFactory, RatingsCycleRepo ratingsCycleRepo) {
        this.employeesRepo = employeesRepo;
        this.ratingsRepo = ratingsRepo;
        this.ratingFactory = ratingFactory;
        this.ratingsCycleRepo = ratingsCycleRepo;
    }

    @Override
    public RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto) {

        // Fetch Employees
        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Employees ratedBy = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("RatedBy employee not found"));

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

        // Getting role from the ENUM and storing it as derivedRole
        RatingRoles derivedRole = deriveRole(ratedBy, employee);

        // Fetching Rating Strategy
        RatingStrategy strategy = ratingFactory.getStrategy(String.valueOf(derivedRole));

        // Fetching Strategy Logic
        Ratings rating = strategy.giveRating(ratingsRequestDto);

        // Set remaining fields
        rating.setRatingId(ratingsRequestDto.getRatingId());
        rating.setEmployee(employee);
        rating.setRatedBy(ratedBy);
        rating.setRatingRole(derivedRole);
        rating.setRatingsCycle(cycle);
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

        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Employees ratedBy = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("Rated By Employee not found"));

        RatingsCycle cycle = ratingsCycleRepo.findByCycleNameIgnoreCase(ratingsRequestDto.getRatings_cycle())
                .orElseThrow(() -> new ResourceNotFoundException("Ratings Cycle not found"));


        rating.setEmployee(employee);
        rating.setRatedBy(ratedBy);
        rating.setRatingStatus(RatingStatus.SUBMITTED);
        rating.setRatingsCycle(cycle);
        rating.setRatingValue(ratingsRequestDto.getRating_value());
        ratingsRepo.save(rating);

        return new RatingsResponseDto(rating);
    }

    @Override
    public RatingsResponseDto approveRating(String ratingId, RatingsRequestDto ratingsRequestDto) {

        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));

        if (rating.getRatingStatus() == null || rating.getRatingStatus() != RatingStatus.SUBMITTED && rating.getRatingStatus() != RatingStatus.APPROVED_BY_TL) {
            throw new IllegalArgumentException("Rating must be in SUBMITTED or APPROVED_BY_TL status to proceed with approval.");
        }

        Employees ratedEmployee = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Employees approver = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("Rated By Employee not found"));

        String approverRole = approver.getRole().getRoleId();
        String ratedRole = ratedEmployee.getRole().getRoleId();

        if (!approver.getTeam().getTeamId().equals(ratedEmployee.getTeam().getTeamId())) {
            throw new IllegalArgumentException("Approver and rated employee must belong to the same team");
        }

        if ("R103".equalsIgnoreCase(ratedRole) || rating.getRatingStatus() == RatingStatus.APPROVED_BY_TL || rating.getRatingStatus() == RatingStatus.SUBMITTED) {
            if ("R102".equalsIgnoreCase(approverRole) && rating.getRatingStatus() == RatingStatus.SUBMITTED) {
                rating.setRatingStatus(RatingStatus.APPROVED_BY_TL);
                Ratings saved = ratingsRepo.save(rating);
                return new RatingsResponseDto(saved);
            }
            if ("R101".equalsIgnoreCase(approverRole)) {
                if (rating.getRatingStatus() != RatingStatus.APPROVED_BY_TL) {
                    throw new IllegalArgumentException("R103 rating must be TL_APPROVED before PM approval");
                }
                rating.setRatingStatus(RatingStatus.APPROVED_BY_PM);
                Ratings saved = ratingsRepo.save(rating);
                return new RatingsResponseDto(saved);
            }
            throw new IllegalArgumentException("R103 ratings must be approved by TL, then PM");
        }
        if ("R102".equalsIgnoreCase(ratedRole)){
            if("R101".equalsIgnoreCase(approverRole)){
                rating.setRatingStatus(RatingStatus.APPROVED_BY_PM);
                Ratings saved = ratingsRepo.save(rating);
                return new RatingsResponseDto(saved);
            }
        }
        throw new IllegalArgumentException("You are not allowed to approve this rating");
    }

    @Override
    public RatingsResponseDto broadcastRating(String ratingId) {
        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));

        if (rating.getRatingStatus() == null || rating.getRatingStatus() != RatingStatus.APPROVED_BY_TL && rating.getRatingStatus() != RatingStatus.APPROVED_BY_PM) {
            throw new IllegalArgumentException("Only APPROVED ratings can be broadcasted");
        }

        Employees broadcaster = rating.getRatedBy();
        Employees approvedEmployee = rating.getEmployee();

        String broadcasterRole = broadcaster.getRole().getRoleId();
        String approvedRole = approvedEmployee.getRole().getRoleId();

        if (broadcaster.getTeam() == null || approvedEmployee.getTeam() == null ||
                !broadcaster.getTeam().getTeamId().equals(approvedEmployee.getTeam().getTeamId())) {
            throw new IllegalArgumentException("Broadcaster and approved employee must belong to the same team");
        }

        if ("R103".equalsIgnoreCase(approvedRole) || rating.getRatingStatus() == RatingStatus.APPROVED_BY_TL) {
            if ("R101".equalsIgnoreCase(broadcasterRole)) {
                rating.setRatingStatus(RatingStatus.BROADCASTED);
                Ratings saved = ratingsRepo.save(rating);
                return new RatingsResponseDto(saved);
            }
        }
        if ("R102".equalsIgnoreCase(approvedRole) || rating.getRatingStatus() == RatingStatus.APPROVED_BY_PM) {
            if (!"R101".equalsIgnoreCase(broadcasterRole)) {
                rating.setRatingStatus(RatingStatus.BROADCASTED);
                Ratings saved = ratingsRepo.save(rating);
                return new RatingsResponseDto(saved);
            }
        }
        throw new IllegalArgumentException("Only PM is allowed to BROADCAST this rating");
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


    private RatingRoles deriveRole(Employees ratedBy, Employees employee) {
        String ratedByRole = ratedBy.getRole().getRoleId();
        String employeeRole = employee.getRole().getRoleId();

        if (ratedBy.getEmployeeId().equalsIgnoreCase(employee.getEmployeeId())) {
            return RatingRoles.SELF;
        }
        if ("R101".equalsIgnoreCase(ratedByRole) && "R103".equalsIgnoreCase(employeeRole))
            return RatingRoles.PM_TO_EMPLOYEE;
        if ("R101".equalsIgnoreCase(ratedByRole) && "R102".equalsIgnoreCase(employeeRole))
            return RatingRoles.PM_TO_TL;
        if ("R102".equalsIgnoreCase(ratedByRole) && "R103".equalsIgnoreCase(employeeRole))
            return RatingRoles.TL_TO_EMPLOYEE;
        throw new APIException("Rating combination is Invalid between " +
                ratedBy.getName() + " (" + ratedByRole + ") and " +
                employee.getName() + " (" + employeeRole + ")");
    }
}


