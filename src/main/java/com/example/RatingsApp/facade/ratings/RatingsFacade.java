package com.example.RatingsApp.facade.ratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.OptionalDouble;

public interface RatingsFacade {

   // RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getAllRatings();

    RatingsResponseDto getRatingById(Long ratingId);

    RatingsResponseDto updateRating(Long ratingId, RatingsRequestDto ratingsRequestDto);

    void deleteRating(Long ratingId);

    List<RatingsResponseDto> getRatingsByCycles(String ratingsCycle);

  //  RatingsResponseDto approveRating(String ratingId,RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto broadcastRating(Long ratingId, RatingsRequestDto ratingsRequestDto);

    List<RatingsResponseDto> getReceivedRatings(Long employeeId);

    List<RatingsResponseDto> getGivenRatings(Long ratedById);

    OptionalDouble getAverageByCycle(String cycleName);

    OptionalDouble getAverageByTeam(Long teamId);

    RatingsResponseDto createSelfRating(@Valid RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto createTlToIndividualRating(@Valid RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto createPmToIndividualRating(@Valid RatingsRequestDto ratingsRequestDto);

    RatingsResponseDto createPmToTlRating(@Valid RatingsRequestDto ratingsRequestDto);
}
