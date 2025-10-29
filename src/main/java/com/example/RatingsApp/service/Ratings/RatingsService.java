package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;

public interface RatingsService {
    RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto);
}
