package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleRequestDto;
import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleResponseDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.service.RatingsCycle.RatingsCycleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratingsCycle")
public class RatingsCycleController {

    private final RatingsCycleService ratingsCycleService;

    public RatingsCycleController(RatingsCycleService ratingsCycleService) {
        this.ratingsCycleService = ratingsCycleService;
    }

    @PostMapping
    public ResponseEntity<RatingsCycleResponseDto> createRatingsCycle(@Valid @RequestBody RatingsCycleRequestDto ratingsCycleRequestDto){
        RatingsCycleResponseDto savedRatingsCycle = ratingsCycleService.createRatingsCycle(ratingsCycleRequestDto);
        return new ResponseEntity<>(savedRatingsCycle, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RatingsCycleResponseDto>> getAllCycles(){
        List<RatingsCycleResponseDto> listCycles = ratingsCycleService.getAllCycles();
        return ResponseEntity.ok(listCycles);
    }

    @DeleteMapping("{ratingsCycleId}")
    public ResponseEntity<String> deleteRatingsCycle(@PathVariable String ratingsCycleId) {
        ratingsCycleService.deleteRatingsCycle(ratingsCycleId);
        return ResponseEntity.ok("Ratings Cycle Deleted!");
    }
}
