package com.example.RatingsApp.strategyimpl;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.enums.RatingStatus;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.stereotype.Component;

@Component("SELF")
public class SelfRating implements RatingStrategy {

    @Override
    public Ratings giveRating(RatingsRequestDto ratingsRequestDto) {

        if (!ratingsRequestDto.getEmployee_id().equals(ratingsRequestDto.getRated_by_id())) {
            throw new APIException("Invalid Self Rating for the user.");
        }

        Ratings ratings = new Ratings();

        ratings.setRatingStatus(RatingStatus.SUBMITTED);
        ratings.setRatingValue(ratingsRequestDto.getRating_value());

        return ratings;
    }
}
