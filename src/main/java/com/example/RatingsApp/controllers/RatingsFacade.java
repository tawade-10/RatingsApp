//package com.example.RatingsApp.controllers;
//
//import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
//import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
//import com.example.RatingsApp.service.Employees.EmployeesService;
//import com.example.RatingsApp.service.Ratings.RatingsService;
//import com.example.RatingsApp.service.Roles.RolesService;
//import com.example.RatingsApp.service.Teams.TeamsService;
//
//public class RatingsFacade {
//
//    private final RatingsService ratingsService;
//
//    private final EmployeesService employeesService;
//
//    private final RolesService rolesService;
//
//    private final TeamsService teamsService;
//
//    public RatingsFacade(RatingsService ratingsService, EmployeesService employeesService, RolesService rolesService, TeamsService teamsService) {
//        this.ratingsService = ratingsService;
//        this.employeesService = employeesService;
//        this.rolesService = rolesService;
//        this.teamsService = teamsService;
//    }
//
//    public RatingsResponseDto createRating(RatingsRequestDto ratingsRequestDto) {
//        employeesService.getEmployeeById(ratingsRequestDto.getEmployee_id());
//        return ratingsService.createRating(ratingsRequestDto);
//    }
//
//}
