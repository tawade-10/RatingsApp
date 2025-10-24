package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.service.RatingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
