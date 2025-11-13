package com.example.RatingsApp.dto.RatingsDto;


// import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.enums.RatingStatus;

public class RatingsRequestDto {

    private String ratingId;

  //  private RatingRoles rating_role;

    private String employee_id;

    private String rated_by_id;

    private int rating_value;

  //  private RatingStatus rating_status;

    private String ratings_cycle;

    public RatingsRequestDto(String ratingId, String employee_id, String rated_by_id, int rating_value, String ratings_cycle) {
        this.ratingId = ratingId;
    //    this.rating_role = rating_role;
        this.employee_id = employee_id;
        this.rated_by_id = rated_by_id;
        this.rating_value = rating_value;
       // this.rating_status = rating_status;
        this.ratings_cycle = ratings_cycle;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

//    public RatingRoles getRating_role() {
//        return rating_role;
//    }
//
//    public void setRating_role(RatingRoles rating_role) {
//        this.rating_role = rating_role;
//    }

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

//    public RatingStatus getRating_status() {
//        return rating_status;
//    }
//
//    public void setRating_status(RatingStatus rating_status) {
//        this.rating_status = rating_status;
//    }


    public String getRatings_cycle() {
        return ratings_cycle;
    }

    public void setRatings_cycle(String ratings_cycle) {
        this.ratings_cycle = ratings_cycle;
    }
}
