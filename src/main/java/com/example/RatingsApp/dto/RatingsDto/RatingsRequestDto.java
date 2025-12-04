package com.example.RatingsApp.dto.RatingsDto;

import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.enums.RatingTypes;

public class RatingsRequestDto {

    private RatingTypes rating_types;
    private Long employee_id;
    private Long rated_by_id;
    private int rating_value;

    public RatingsRequestDto(Ratings ratings) {
        this.rating_types = ratings.getRatingTypes();
        this.employee_id = ratings.getEmployee().getEmployeeId();
        this.rated_by_id = ratings.getRatedBy().getEmployeeId();
        this.rating_value = ratings.getRatingValue();
    }

    public RatingsRequestDto() {}

    public RatingTypes getRating_types() {
        return rating_types;
    }

    public void setRating_types(RatingTypes rating_types) {
        this.rating_types = rating_types;
    }

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public Long getRated_by_id() {
        return rated_by_id;
    }

    public void setRated_by_id(Long rated_by_id) {
        this.rated_by_id = rated_by_id;
    }

    public int getRating_value() {
        return rating_value;
    }

    public void setRating_value(int rating_value) {
        this.rating_value = rating_value;
    }
}
