package com.example.RatingsApp.facade.ratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import java.util.List;
import java.util.OptionalDouble;

public interface RatingsFacade {

    RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getAllRatings();

    RatingsResponseDto getRatingById(String ratingId);

    RatingsResponseDto updateRating(String ratingId, RatingsRequestDto ratingsRequestDto);

    void deleteRating(String ratingId);

    List<RatingsResponseDto> getRatingsByCycles(String ratingsCycle);

    RatingsResponseDto approveRating(String ratingId,RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto broadcastRating(String ratingId, RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getReceivedRatings(String employeeId);

    List<RatingsResponseDto> getGivenRatings(String ratedById);

    OptionalDouble getAverageByCycle(String cycleName);

    OptionalDouble getAverageByTeam(String teamId);
}
