package com.example.RatingsApp.strategy;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.entity.Ratings;

public interface RatingStrategy {

    Ratings giveRating(RatingsRequestDto ratingsRequestDto);
}
