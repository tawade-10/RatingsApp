package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.Factory.RatingFactory;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
// import com.example.RatingsApp.repository.RatingsCycleRepo;
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

   // private final RatingsCycleRepo ratingsCycleRepo;

    public RatingsServiceImpl(EmployeesRepo employeesRepo, RatingsRepo ratingsRepo, RatingFactory ratingFactory) {
        this.employeesRepo = employeesRepo;
        this.ratingsRepo = ratingsRepo;
        this.ratingFactory = ratingFactory;
       // this.ratingsCycleRepo = ratingsCycleRepo;
    }

    @Override
    public RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto) {

        // Fetch the two employees
        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Employees ratedBy = employeesRepo.findByEmployeeIdIgnoreCase(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("RatedBy employee not found"));

        if(ratingsRequestDto.getRatingId() == null || Objects.equals(ratingsRequestDto.getRatingId(), ""))
            throw new IllegalArgumentException("Rating Id cannot be null or empty");

        Optional<Ratings> existingRatingId = ratingsRepo.findByRatingId(ratingsRequestDto.getRatingId());
        if(existingRatingId.isPresent()) {
            throw new APIException("Rating with ID : '" + ratingsRequestDto.getRatingId() + "' already exists!");
        }

        //Get roles by instantiating the RatingRoles ENUM
        RatingRoles derivedRole = deriveRatingRole(ratedBy, employee);

        //The fetched role will be taken by getStrategy method
        RatingStrategy strategy = ratingFactory.getStrategy(String.valueOf(derivedRole));

        //For fetching the logic for that particular rating
        Ratings rating = strategy.giveRating(ratingsRequestDto);

        rating.setRatingId(ratingsRequestDto.getRatingId());
        rating.setEmployee(employee);
        rating.setRatedBy(ratedBy);
        rating.setRatingRole(derivedRole);
        // rating.setRatingsCycle();
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

//        RatingsCycle ratingsCycle = ratingsCycleRepo.findByCycleName(ratingsRequestDto.getCycle_name())
//                .orElseThrow(() -> new ResourceNotFoundException("Cycle not found"));
//        rating.setRatingCycle(ratingsCycle);

        rating.setRatingStatus(RatingStatus.SUBMITTED);
        rating.setRatingsCycle(ratingsRequestDto.getRatings_cycle());
        rating.setRatingValue(ratingsRequestDto.getRating_value());

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
    public List<RatingsResponseDto> getRatingsByCycles(String ratingCycles) {
        List<Ratings> ratings = ratingsRepo.findAll(Sort.by(Sort.Direction.ASC, "ratingCycles"));
        return ratings.stream().map(RatingsResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public RatingsResponseDto approveRating(String ratingId) {
        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));

        if (rating.getRatingStatus() == null || rating.getRatingStatus() != RatingStatus.SUBMITTED) {
            throw new IllegalArgumentException("Only SUBMITTED ratings can be approved");
        }

        Employees approver = rating.getRatedBy();
        Employees ratedEmployee = rating.getEmployee();

        String approverRole = approver.getRole().getRoleId();
        String ratedRole = ratedEmployee.getRole().getRoleId();

        if (approver.getTeam() == null || ratedEmployee.getTeam() == null ||
                !approver.getTeam().getTeamId().equals(ratedEmployee.getTeam().getTeamId())) {
            throw new IllegalArgumentException("Approver and rated employee must belong to the same team");
        }

        if ("R103".equalsIgnoreCase(ratedRole)) {
            if (!"R102".equalsIgnoreCase(approverRole)) {
                throw new IllegalArgumentException("Only a TL can approve an Individual’s rating");
            }
        }
        else if ("R102".equalsIgnoreCase(ratedRole)) {
            if (!"R101".equalsIgnoreCase(approverRole)) {
                throw new IllegalArgumentException("Only a PM can approve a Team Lead’s self rating");
            }
        }

        rating.setRatingStatus(RatingStatus.APPROVED);
        ratingsRepo.save(rating);

        return new RatingsResponseDto(rating);
    }


    @Override
    public RatingsResponseDto broadcastRating(String ratingId) {
        Ratings rating = ratingsRepo.findByRatingId(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));

        if (rating.getRatingStatus() == null || rating.getRatingStatus() != RatingStatus.APPROVED) {
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

        if ("R102".equalsIgnoreCase(approvedRole)) {
            if (!"R101".equalsIgnoreCase(broadcasterRole)) {
                throw new IllegalArgumentException("Only a PM can approve an TL’s rating");
            }
        }
//        else if ("R102".equalsIgnoreCase(ratedRole)) {
//            if (!"R101".equalsIgnoreCase(approverRole)) {
//                throw new IllegalArgumentException("Only a PM can approve a Team Lead’s self rating");
//            }
//        }

        rating.setRatingStatus(RatingStatus.BROADCASTED);
        Ratings savedRating = ratingsRepo.save(rating);

        return new RatingsResponseDto(savedRating);
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


    private RatingRoles deriveRatingRole(Employees ratedBy, Employees employee) {
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

