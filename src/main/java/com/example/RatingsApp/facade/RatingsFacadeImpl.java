package com.example.RatingsApp.facade;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.facade.RatingsFacade;
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
}
