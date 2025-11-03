package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.facade.RatingsFacade;
import com.example.RatingsApp.service.Ratings.RatingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    private final RatingsFacade ratingsFacade;

    public RatingsController(RatingsFacade ratingsFacade) {
        this.ratingsFacade = ratingsFacade;
    }

    @PostMapping
    public ResponseEntity<RatingsResponseDto> createRating(@RequestBody RatingsRequestDto ratingsRequestDto) {
        RatingsResponseDto savedRating = ratingsFacade.createRating(ratingsRequestDto);
        return new ResponseEntity<>(savedRating, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RatingsResponseDto>> getAllRatings() {
        List<RatingsResponseDto> listRatings = ratingsFacade.getAllRatings();
        return ResponseEntity.ok(listRatings);
    }

    @GetMapping("{ratingId}")
    public ResponseEntity<RatingsResponseDto> getRatingById(@PathVariable Long ratingId) {
        RatingsResponseDto getRatings = ratingsFacade.getRatingById(ratingId);
        return ResponseEntity.ok(getRatings);
    }

    @PutMapping("{ratingId}")
    public ResponseEntity<RatingsResponseDto> updateRating(@PathVariable Long ratingId, @RequestBody RatingsRequestDto ratingsRequestDto) {
        RatingsResponseDto updatedRating = ratingsFacade.updateRating(ratingId, ratingsRequestDto);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable Long ratingId) {
        ratingsFacade.deleteRating(ratingId);
        return ResponseEntity.ok("Rating Deleted!");
    }
}
