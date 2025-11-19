package com.example.RatingsApp.service.RatingsCycle;

import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleRequestDto;
import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleResponseDto;

import java.util.List;

public interface RatingsCycleService {
    RatingsCycleResponseDto createRatingsCycle(RatingsCycleRequestDto ratingsCycleRequestDto);

    List<RatingsCycleResponseDto> getAllCycles();

    RatingsCycleResponseDto deleteRatingsCycle(String ratingsCycleId);
}
