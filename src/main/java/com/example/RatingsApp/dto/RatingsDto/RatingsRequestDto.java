package com.example.RatingsApp.dto.RatingsDto;

import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.enums.RatingRoles;
import com.example.RatingsApp.entity.enums.RatingStatus;

public class RatingsRequestDto {

    private RatingRoles rating_role;

    private Long employee_id;

    private Long rated_by_id;

    private int rating_value;

    private RatingStatus rating_status;

    public RatingsRequestDto(RatingRoles rating_role, Long employee_id, Long rated_by_id, int rating_value, RatingStatus rating_status) {
        this.rating_role = rating_role;
        this.employee_id = employee_id;
        this.rated_by_id = rated_by_id;
        this.rating_value = rating_value;
        this.rating_status = rating_status;
    }

    public RatingRoles getRating_role() {
        return rating_role;
    }

    public void setRating_role(RatingRoles rating_role) {
        this.rating_role = rating_role;
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

    public RatingStatus getRating_status() {
        return rating_status;
    }

    public void setRating_status(RatingStatus rating_status) {
        this.rating_status = rating_status;
    }
}
