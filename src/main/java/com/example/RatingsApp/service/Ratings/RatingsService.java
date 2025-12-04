package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;

import java.util.List;
import java.util.OptionalDouble;

public interface RatingsService {
    RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getAllRatings();

    RatingsResponseDto getRatingById(Long ratingsId);

    RatingsResponseDto updateRating(Long ratingId, RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto deleteRating(Long ratingId);

    List<RatingsResponseDto> getRatingsByCycles(String ratingsCycle);

   // RatingsResponseDto approveRating(String ratingId,RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto broadcastRating(Long ratingId, RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getReceivedRatings(Long employeeId);

    List<RatingsResponseDto> getGivenRatings(Long ratedById);

    OptionalDouble getAverageByCycle(String cycleName);

    OptionalDouble getAverageByTeam(Long teamId);

//    RatingsResponseDto createSelfRating(RatingsRequestDto ratingsRequestDto);
//
//    RatingsResponseDto createTlToIndividualRating(RatingsRequestDto ratingsRequestDto);
//
//    RatingsResponseDto createPmToIndividualRating(RatingsRequestDto ratingsRequestDto);
//
//    RatingsResponseDto createPmToTlRating(RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto getPendingRatings(Long teamId);
}
