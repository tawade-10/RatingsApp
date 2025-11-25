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
    public RatingsResponseDto getRatingById(Long ratingId) {
        return ratingsService.getRatingById(ratingId);
    }

    @Override
    public RatingsResponseDto updateRating(Long ratingId, RatingsRequestDto ratingsRequestDto) {
        return ratingsService.updateRating(ratingId, ratingsRequestDto);
    }

    @Override
    public void deleteRating(Long ratingId) {
        ratingsService.deleteRating(ratingId);
    }

    @Override
    public List<RatingsResponseDto> getRatingsByCycles(String ratingsCycle) {
        return ratingsService.getRatingsByCycles(ratingsCycle);
    }

//    @Override
//    public RatingsResponseDto approveRating(String ratingId,RatingsRequestDto ratingsRequestDto) {
//        return ratingsService.approveRating(ratingId,ratingsRequestDto);
//    }

    @Override
    public RatingsResponseDto broadcastRating(Long ratingId, RatingsRequestDto ratingsRequestDto) {
        return ratingsService.broadcastRating(ratingId,ratingsRequestDto);
    }

    @Override
    public List<RatingsResponseDto> getReceivedRatings(Long employeeId) {
        return ratingsService.getReceivedRatings(employeeId);
    }

    @Override
    public List<RatingsResponseDto> getGivenRatings(Long ratedById) {
        return ratingsService.getGivenRatings(ratedById);
    }

    @Override
    public OptionalDouble getAverageByCycle(String cycleName) {
        return ratingsService.getAverageByCycle(cycleName);
    }

    @Override
    public OptionalDouble getAverageByTeam(Long teamId) {
        return ratingsService.getAverageByTeam(teamId);
    }
}
