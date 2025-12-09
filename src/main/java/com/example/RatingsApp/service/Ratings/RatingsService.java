package com.example.RatingsApp.service.Ratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;

import java.util.List;
import java.util.OptionalDouble;

public interface RatingsService {
    RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getAllRatings();

    RatingsResponseDto getRatingById(String ratingsId);

    RatingsResponseDto updateRating(String ratingId, RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto deleteRating(String ratingId);

    List<RatingsResponseDto> getRatingsByCycles(String ratingsCycle);

   // RatingsResponseDto approveRating(String ratingId,RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto broadcastRating(String ratingId, RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getReceivedRatings(String employeeId);

    List<RatingsResponseDto> getGivenRatings(String ratedById);

    OptionalDouble getAverageByCycle(String cycleName);

    OptionalDouble getAverageByTeam(String teamId);

//    RatingsResponseDto createSelfRating(RatingsRequestDto ratingsRequestDto);
//
//    RatingsResponseDto createTlToIndividualRating(RatingsRequestDto ratingsRequestDto);
//
//    RatingsResponseDto createPmToIndividualRating(RatingsRequestDto ratingsRequestDto);
//
//    RatingsResponseDto createPmToTlRating(RatingsRequestDto ratingsRequestDto);

//    List<RatingsResponseDto> getPendingRatings(String teamId);

    List<RatingsResponseDto> getBroadcastedRatings();

    List<RatingsResponseDto> getRatingsByTeam(String teamId);

    List<RatingsResponseDto> getSelfRatings();
}
