package com.example.RatingsApp.service.RatingsCycle;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleRequestDto;
import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleResponseDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.RatingsCycleRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingsCycleServiceImpl implements RatingsCycleService{

    private final RatingsCycleRepo ratingsCycleRepo;

    public RatingsCycleServiceImpl(RatingsCycleRepo ratingsCycleRepo) {
        this.ratingsCycleRepo = ratingsCycleRepo;
    }

    @Override
    public RatingsCycleResponseDto createRatingsCycle(RatingsCycleRequestDto ratingsCycleRequestDto) {

        RatingsCycle ratingsCycle = new RatingsCycle();

        Optional<RatingsCycle> existingRatingsCycle = ratingsCycleRepo.findByCycleNameIgnoreCase(ratingsCycleRequestDto.getCycleName());
        if (existingRatingsCycle.isPresent()) {
            throw new APIException(" Ratings Cycle '" + ratingsCycleRequestDto.getCycleName() + "' already exists!");
        }

        ratingsCycle.setCycleId(ratingsCycleRequestDto.getCycleId());
        ratingsCycle.setCycleName(ratingsCycleRequestDto.getCycleName());
        ratingsCycle.setStartDate(ratingsCycleRequestDto.getStartDate());
        ratingsCycle.setEndDate(ratingsCycleRequestDto.getEndDate());
        ratingsCycle.setStatus(ratingsCycleRequestDto.getStatus());

        RatingsCycle savedRating = ratingsCycleRepo.save(ratingsCycle);
        return new RatingsCycleResponseDto(savedRating);
    }

    @Override
    public List<RatingsCycleResponseDto> getAllCycles() {
        List<RatingsCycle> listRatings = ratingsCycleRepo.findAll(Sort.by(Sort.Direction.ASC, "cycleId"));
        return listRatings.stream().map(RatingsCycleResponseDto::new).collect(Collectors.toList());
    }



}


