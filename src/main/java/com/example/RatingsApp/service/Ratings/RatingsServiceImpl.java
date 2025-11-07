package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.Factory.RatingFactory;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
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

    public RatingsServiceImpl(EmployeesRepo employeesRepo, RatingsRepo ratingsRepo, RatingFactory ratingFactory) {
        this.employeesRepo = employeesRepo;
        this.ratingsRepo = ratingsRepo;
        this.ratingFactory = ratingFactory;
    }

    @Override
    public RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto) {
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

        if(ratingsRequestDto.getRating_cycles() == null || Objects.equals(ratingsRequestDto.getRatingId(), ""))
            throw new IllegalArgumentException("Rating Cycle cannot be null or empty");

//        Optional<Ratings> existingRatingCycle = ratingsRepo.findByRatingCycles(ratingsRequestDto.getRating_cycles());
//        if(existingRatingCycle.isPresent()) {
//            throw new APIException("Rating Cycle : '" + ratingsRequestDto.getRatingId() + "' already exists!");
//        }


        //For fetching the type of rating in String Format
        RatingStrategy strategy = ratingFactory.getStrategy(ratingsRequestDto.getRating_role().toString());

        //For fetching the logic for that particular rating
        Ratings rating = strategy.giveRating(ratingsRequestDto);

        rating.setRatingId(ratingsRequestDto.getRatingId());
        rating.setEmployee(employee);
        rating.setRatedBy(ratedBy);

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

        rating.setRatingStatus(ratingsRequestDto.getRating_status());
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
}
