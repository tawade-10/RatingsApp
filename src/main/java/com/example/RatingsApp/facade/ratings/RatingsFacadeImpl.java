package com.example.RatingsApp.facade.ratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.service.Ratings.RatingsService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.OptionalDouble;

@Component
public class RatingsFacadeImpl implements RatingsFacade {

    private final RatingsService ratingsService;

    public RatingsFacadeImpl(RatingsService ratingsService) {
        this.ratingsService = ratingsService;
    }

    @Override
    public RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto) {
        return ratingsService.createRating(ratingsRequestDto);
    }

    @Override
    public List<RatingsResponseDto> getAllRatings() {
        return ratingsService.getAllRatings();
    }

    @Override
    public RatingsResponseDto getRatingById(String ratingId) {
        return ratingsService.getRatingById(ratingId);
    }

    @Override
    public RatingsResponseDto updateRating(String ratingId, RatingsRequestDto ratingsRequestDto) {
        return ratingsService.updateRating(ratingId, ratingsRequestDto);
    }

    @Override
    public void deleteRating(String ratingId) {
        ratingsService.deleteRating(ratingId);
    }

    @Override
    public List<RatingsResponseDto> getRatingsByCycles(String ratingsCycle) {
        return ratingsService.getRatingsByCycles(ratingsCycle);
    }

    @Override
    public RatingsResponseDto broadcastRating(String ratingId, RatingsRequestDto ratingsRequestDto) {
        return ratingsService.broadcastRating(ratingId,ratingsRequestDto);
    }

    @Override
    public List<RatingsResponseDto> getReceivedRatings(String employeeId) {
        return ratingsService.getReceivedRatings(employeeId);
    }

    @Override
    public List<RatingsResponseDto> getGivenRatings(String ratedById) {
        return ratingsService.getGivenRatings(ratedById);
    }

    @Override
    public OptionalDouble getAverageByCycle(String cycleName) {
        return ratingsService.getAverageByCycle(cycleName);
    }

    @Override
    public OptionalDouble getAverageByTeam(String teamId) {
        return ratingsService.getAverageByTeam(teamId);
    }

//    @Override
//    public RatingsResponseDto createSelfRating(RatingsRequestDto ratingsRequestDto) {
//        return ratingsService.createSelfRating(ratingsRequestDto);
//    }
//
//    @Override
//    public RatingsResponseDto createTlToIndividualRating(RatingsRequestDto ratingsRequestDto) {
//        return ratingsService.createTlToIndividualRating(ratingsRequestDto);
//    }
//
//    @Override
//    public RatingsResponseDto createPmToIndividualRating(RatingsRequestDto ratingsRequestDto) {
//        return ratingsService.createPmToIndividualRating(ratingsRequestDto);
//    }
//
//    @Override
//    public RatingsResponseDto createPmToTlRating(RatingsRequestDto ratingsRequestDto) {
//        return ratingsService.createPmToTlRating(ratingsRequestDto);
//    }

    @Override
    public List<RatingsResponseDto> getRatingsByTeam(String teamId) {
        return ratingsService.getRatingsByTeam(teamId);
    }

    @Override
    public List<RatingsResponseDto> getSelfRatings() {
        return ratingsService.getSelfRatings();
    }

    @Override
    public List<RatingsResponseDto> getBroadcastedRatings() {
        return ratingsService.getBroadcastedRatings();
    }
}
