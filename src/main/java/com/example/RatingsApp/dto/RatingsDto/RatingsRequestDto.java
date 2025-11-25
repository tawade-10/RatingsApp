package com.example.RatingsApp.dto.RatingsDto;

import com.example.RatingsApp.entity.Ratings;

public class RatingsRequestDto {

//    private String ratingId;

  //  private RatingRoles rating_role;

    private Long employee_id;

    private Long rated_by_id;

    private int rating_value;

    private String ratings_cycle;

    public RatingsRequestDto(Ratings ratings) {
    //    this.ratingId = ratings.getRatingId();
    //    this.rating_role = rating_role;
        this.employee_id = ratings.getEmployee().getEmployeeId();
        this.rated_by_id = ratings.getRatedBy().getEmployeeId();
        this.rating_value = ratings.getRatingValue();
        this.ratings_cycle = ratings.getRatingsCycle().getCycleName();
    }

    public RatingsRequestDto() {
    }

//    public String getRatingId() {
//        return ratingId;
//    }
//
//    public void setRatingId(String ratingId) {
//        this.ratingId = ratingId;
//    }

//    public RatingRoles getRating_role() {
//        return rating_role;
//    }
//
//    public void setRating_role(RatingRoles rating_role) {
//        this.rating_role = rating_role;
//    }

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

    public String getRatings_cycle() {
        return ratings_cycle;
    }

    public void setRatings_cycle(String ratings_cycle) {
        this.ratings_cycle = ratings_cycle;
    }
}
