package com.example.RatingsApp.facade;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import java.util.List;

public interface RatingsFacade {

    RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getAllRatings();

    RatingsResponseDto getRatingById(Long ratingId);

    RatingsResponseDto updateRating(Long ratingId, RatingsRequestDto ratingsRequestDto);

    void deleteRating(Long ratingId);
}
