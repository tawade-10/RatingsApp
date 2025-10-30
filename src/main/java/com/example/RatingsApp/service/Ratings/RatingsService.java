package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;

import java.util.List;

public interface RatingsService {
    RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getAllRatings();

    RatingsResponseDto getRatingById(Long ratingsId);

    RatingsResponseDto updateRating(Long ratingId, RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto deleteRating(Long ratingId);
}
