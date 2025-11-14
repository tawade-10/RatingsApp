package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.facade.ratings.RatingsFacade;
import jakarta.validation.Valid;
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
    public ResponseEntity<RatingsResponseDto> createRating(@Valid @RequestBody RatingsRequestDto ratingsRequestDto) {
        RatingsResponseDto savedRating = ratingsFacade.createRating(ratingsRequestDto);
        return new ResponseEntity<>(savedRating, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RatingsResponseDto>> getAllRatings() {
        List<RatingsResponseDto> listRatings = ratingsFacade.getAllRatings();
        return ResponseEntity.ok(listRatings);
    }

    @GetMapping("{ratingId}")
    public ResponseEntity<RatingsResponseDto> getRatingById(@PathVariable String ratingId) {
        RatingsResponseDto getRatings = ratingsFacade.getRatingById(ratingId);
        return ResponseEntity.ok(getRatings);
    }

    @GetMapping("/received/{employeeId}")
    public ResponseEntity<List<RatingsResponseDto>> getReceivedRatings(@PathVariable String employeeId){
        List<RatingsResponseDto> receivedRatings = ratingsFacade.getReceivedRatings(employeeId);
        return ResponseEntity.ok(receivedRatings);
    }

    @GetMapping("/given/{ratedById}")
    public ResponseEntity<List<RatingsResponseDto>> getGivenRatings(@PathVariable String ratedById){
        List<RatingsResponseDto> givenRatings = ratingsFacade.getGivenRatings(ratedById);
        return ResponseEntity.ok(givenRatings);
    }

    @GetMapping("/ratingCycle/{ratingsCycle}")
    public ResponseEntity<List<RatingsResponseDto>> getRatingsByCycles(@PathVariable String ratingsCycle){
        List<RatingsResponseDto> getRatings = ratingsFacade.getRatingsByCycles(ratingsCycle);
        return ResponseEntity.ok(getRatings);
    }

    @PutMapping("{ratingId}")
    public ResponseEntity<RatingsResponseDto> updateRating(@PathVariable String ratingId, @RequestBody RatingsRequestDto ratingsRequestDto) {
        RatingsResponseDto updatedRating = ratingsFacade.updateRating(ratingId, ratingsRequestDto);
        return ResponseEntity.ok(updatedRating);
    }

    @PutMapping("/{ratingId}/approve")
    public ResponseEntity<RatingsResponseDto> approveRating(@PathVariable String ratingId) {
        RatingsResponseDto approvedRating = ratingsFacade.approveRating(ratingId);
        return ResponseEntity.ok(approvedRating);
    }

    @PutMapping("{ratingId}/broadcast")
    public ResponseEntity<RatingsResponseDto> broadcastRating(@PathVariable String ratingId) {
        RatingsResponseDto broadcastedRating = ratingsFacade.broadcastRating(ratingId);
        return ResponseEntity.ok(broadcastedRating);
    }

    @DeleteMapping("{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable String ratingId) {
        ratingsFacade.deleteRating(ratingId);
        return ResponseEntity.ok("Rating Deleted!");
    }
}
