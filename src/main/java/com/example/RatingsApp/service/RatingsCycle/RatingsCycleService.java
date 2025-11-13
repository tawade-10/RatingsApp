package com.example.RatingsApp.service.RatingsCycle;

import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleRequestDto;
import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleResponseDto;

public interface RatingsCycleService {
    RatingsCycleResponseDto createRatingsCycle(RatingsCycleRequestDto ratingsCycleRequestDto);
}
