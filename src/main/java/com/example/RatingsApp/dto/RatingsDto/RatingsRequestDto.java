package com.example.RatingsApp.dto.RatingsDto;

import com.example.RatingsApp.entity.enums.RatingCycles;
import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;

public class RatingsRequestDto {

    private String ratingId;

    private RatingRoles rating_role;

    private String employee_id;

    private String rated_by_id;

    private int rating_value;

    private RatingStatus rating_status;

    private RatingCycles rating_cycles;

    public RatingsRequestDto(String ratingId, RatingRoles rating_role, String employee_id, String rated_by_id, int rating_value, RatingStatus rating_status, RatingCycles rating_cycles) {
        this.ratingId = ratingId;
        this.rating_role = rating_role;
        this.employee_id = employee_id;
        this.rated_by_id = rated_by_id;
        this.rating_value = rating_value;
        this.rating_status = rating_status;
        this.rating_cycles = rating_cycles;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public RatingRoles getRating_role() {
        return rating_role;
    }

    public void setRating_role(RatingRoles rating_role) {
        this.rating_role = rating_role;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getRated_by_id() {
        return rated_by_id;
    }

    public void setRated_by_id(String rated_by_id) {
        this.rated_by_id = rated_by_id;
    }

    public int getRating_value() {
        return rating_value;
    }

    public void setRating_value(int rating_value) {
        this.rating_value = rating_value;
    }

    public RatingStatus getRating_status() {
        return rating_status;
    }

    public void setRating_status(RatingStatus rating_status) {
        this.rating_status = rating_status;
    }

    public RatingCycles getRating_cycles() {
        return rating_cycles;
    }

    public void setRating_cycles(RatingCycles rating_cycles) {
        this.rating_cycles = rating_cycles;
    }
}
