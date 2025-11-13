package com.example.RatingsApp.service.RatingsCycle;

import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleRequestDto;
import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleResponseDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.repository.RatingsCycleRepo;
import org.springframework.stereotype.Service;

@Service
public class RatingsCycleServiceImpl implements RatingsCycleService{

    private final RatingsCycleRepo ratingsCycleRepo;

    public RatingsCycleServiceImpl(RatingsCycleRepo ratingsCycleRepo) {
        this.ratingsCycleRepo = ratingsCycleRepo;
    }

    @Override
    public RatingsCycleResponseDto createRatingsCycle(RatingsCycleRequestDto ratingsCycleRequestDto) {

        RatingsCycle ratingsCycle = new RatingsCycle();

        ratingsCycle.setCycleId(ratingsCycleRequestDto.getCycleId());
        ratingsCycle.setCycleName(ratingsCycleRequestDto.getCycleName());
        ratingsCycle.setStartDate(ratingsCycleRequestDto.getStartDate());
        ratingsCycle.setEndDate(ratingsCycleRequestDto.getEndDate());
        ratingsCycle.setStatus(ratingsCycleRequestDto.getStatus());

        RatingsCycle savedRating = ratingsCycleRepo.save(ratingsCycle);
        return new RatingsCycleResponseDto(savedRating);
    }
}


