package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.service.Ratings.RatingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    private final RatingsService ratingsService;

    public RatingsController(RatingsService ratingsService) {
        this.ratingsService = ratingsService;
    }

    @PostMapping
    public ResponseEntity<RatingsResponseDto> createRating (@RequestBody RatingsRequestDto ratingsRequestDto){
        RatingsResponseDto savedRating = ratingsService.createRating(ratingsRequestDto);
        return new ResponseEntity<>(savedRating, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RatingsResponseDto>> getAllRatings (){
        List<RatingsResponseDto> listRatings = ratingsService.getAllRatings();
        return ResponseEntity.ok(listRatings);
    }

    @GetMapping("{ratingId}")
    public ResponseEntity<RatingsResponseDto> getRatingById(@PathVariable Long ratingId){
        RatingsResponseDto getRatings = ratingsService.getRatingById(ratingId);
        return ResponseEntity.ok(getRatings);
    }

    @PutMapping("{ratingId}")
    public ResponseEntity<RatingsResponseDto> updateRating(@PathVariable Long ratingId, @RequestBody RatingsRequestDto ratingsRequestDto){
        RatingsResponseDto updatedRating = ratingsService.updateRating(ratingId,ratingsRequestDto);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable Long ratingId){
        RatingsResponseDto deletedRating = ratingsService.deleteRating(ratingId);
        return ResponseEntity.ok("Rating Deleted!");
    }
}
