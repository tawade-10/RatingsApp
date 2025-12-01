package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.facade.ratings.RatingsFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.OptionalDouble;

@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    private final RatingsFacade ratingsFacade;

    public RatingsController(RatingsFacade ratingsFacade) {
        this.ratingsFacade = ratingsFacade;
    }

//    @PostMapping
//    public ResponseEntity<RatingsResponseDto> createRating(@Valid @RequestBody RatingsRequestDto ratingsRequestDto) {
//        RatingsResponseDto savedRating = ratingsFacade.createRating(ratingsRequestDto);
//        return new ResponseEntity<>(savedRating, HttpStatus.CREATED);
//    }

    @PostMapping("/self")
    public ResponseEntity<RatingsResponseDto> createSelfRating(@Valid @RequestBody RatingsRequestDto ratingsRequestDto) {
        RatingsResponseDto selfRating = ratingsFacade.createSelfRating(ratingsRequestDto);
        return new ResponseEntity<>(selfRating, HttpStatus.CREATED);
    }

    @PostMapping("/tl")
    public ResponseEntity<RatingsResponseDto> createTlToIndividualRating(@Valid @RequestBody RatingsRequestDto ratingsRequestDto) {

        RatingsResponseDto tlToEmployeeRating = ratingsFacade.createTlToIndividualRating(ratingsRequestDto);
        return new ResponseEntity<>(tlToEmployeeRating, HttpStatus.CREATED);
    }

    @PostMapping("/pm/pm_to_individual")
    public ResponseEntity<RatingsResponseDto> createPmToIndividualRating(@Valid @RequestBody RatingsRequestDto ratingsRequestDto) {
        RatingsResponseDto pmToEmployeeRating = ratingsFacade.createPmToIndividualRating(ratingsRequestDto);
        return new ResponseEntity<>(pmToEmployeeRating, HttpStatus.CREATED);
    }

    @PostMapping("/pm/pm_to_tl")
    public ResponseEntity<RatingsResponseDto> createPmToTlRating(@Valid @RequestBody RatingsRequestDto ratingsRequestDto) {
        RatingsResponseDto pmToTlRating = ratingsFacade.createPmToTlRating(ratingsRequestDto);
        return new ResponseEntity<>(pmToTlRating, HttpStatus.CREATED);
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

    @GetMapping("/received/{employeeId}")
    public ResponseEntity<List<RatingsResponseDto>> getReceivedRatings(@PathVariable Long employeeId) {
        List<RatingsResponseDto> receivedRatings = ratingsFacade.getReceivedRatings(employeeId);
        return ResponseEntity.ok(receivedRatings);
    }

    @GetMapping("/given/{ratedById}")
    public ResponseEntity<List<RatingsResponseDto>> getGivenRatings(@PathVariable Long ratedById) {
        List<RatingsResponseDto> givenRatings = ratingsFacade.getGivenRatings(ratedById);
        return ResponseEntity.ok(givenRatings);
    }

    @GetMapping("/ratingCycle/{ratingsCycle}")
    public ResponseEntity<List<RatingsResponseDto>> getRatingsByCycles(@PathVariable String ratingsCycle) {
        List<RatingsResponseDto> getRatings = ratingsFacade.getRatingsByCycles(ratingsCycle);
        return ResponseEntity.ok(getRatings);
    }

    @GetMapping("/average/ratingCycle/{cycleName}")
    public ResponseEntity<OptionalDouble> getAverageByCycle(@PathVariable String cycleName) {
        OptionalDouble average = ratingsFacade.getAverageByCycle(cycleName);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/average/team/{teamId}")
    public ResponseEntity<OptionalDouble> getAverageByTeam(@PathVariable Long teamId) {
        OptionalDouble average = ratingsFacade.getAverageByTeam(teamId);
        return ResponseEntity.ok(average);
    }

    @PutMapping("{ratingId}")
    public ResponseEntity<RatingsResponseDto> updateRating(
            @PathVariable Long ratingId, @RequestBody RatingsRequestDto ratingsRequestDto) {

        RatingsResponseDto updatedRating = ratingsFacade.updateRating(ratingId, ratingsRequestDto);
        return ResponseEntity.ok(updatedRating);
    }

    @PutMapping("/broadcast/{ratingId}")
    public ResponseEntity<RatingsResponseDto> broadcastRating(
            @PathVariable Long ratingId, @RequestBody RatingsRequestDto ratingsRequestDto) {

        RatingsResponseDto broadcastedRating = ratingsFacade.broadcastRating(ratingId, ratingsRequestDto);
        return ResponseEntity.ok(broadcastedRating);
    }

    @DeleteMapping("{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable Long ratingId) {
        ratingsFacade.deleteRating(ratingId);
        return ResponseEntity.ok("Rating Deleted!");
    }
}
