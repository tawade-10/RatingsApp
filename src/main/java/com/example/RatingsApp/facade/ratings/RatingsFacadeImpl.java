package com.example.RatingsApp.facade.ratings;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.service.Ratings.RatingsService;
import org.springframework.stereotype.Component;
import java.util.List;

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
    public List<RatingsResponseDto> getRatingsByCycles(String ratingCycles) {
        return ratingsService.getRatingsByCycles(ratingCycles);
    }

    @Override
    public RatingsResponseDto approveRating(String ratingId) {
        return ratingsService.approveRating(ratingId);
    }

    @Override
    public RatingsResponseDto broadcastRating(String ratingId) {
        return ratingsService.broadcastRating(ratingId);
    }

    @Override
    public List<RatingsResponseDto> getReceivedRatings(String employeeId) {
        return ratingsService.getReceivedRatings(employeeId);
    }

    @Override
    public List<RatingsResponseDto> getGivenRatings(String ratedById) {
        return ratingsService.getGivenRatings(ratedById);
    }
}
