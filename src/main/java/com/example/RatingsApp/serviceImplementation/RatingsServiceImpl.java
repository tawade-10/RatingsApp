package com.example.RatingsApp.serviceImplementation;

import com.example.RatingsApp.Factory.RatingFactory;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RatingsRepo;
import com.example.RatingsApp.service.RatingsService;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.stereotype.Service;

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
        Employees employee = employeesRepo.findById(ratingsRequestDto.getEmployee_id())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        Employees ratedBy = employeesRepo.findById(ratingsRequestDto.getRated_by_id())
                .orElseThrow(() -> new ResourceNotFoundException("RatedBy employee not found"));

        RatingStrategy strategy = ratingFactory.getStrategy(ratingsRequestDto.getRating_role().toString());
        Ratings rating = strategy.giveRating(ratingsRequestDto);
        rating.setEmployee(employee);
        rating.setRatedBy(ratedBy);

        Ratings savedRating = ratingsRepo.save(rating);

        return new RatingsResponseDto(savedRating);
    }

}
