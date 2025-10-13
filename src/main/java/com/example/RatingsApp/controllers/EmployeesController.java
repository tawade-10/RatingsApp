package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesResponseDto;
import com.example.RatingsApp.service.EmployeesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

    private final EmployeesService employeesService;

    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @PostMapping
    public ResponseEntity<EmployeesResponseDto> createRole(@RequestBody EmployeesRequestDto employeesRequestDto) {
        EmployeesResponseDto savedEmployee = employeesService.createEmployee(employeesRequestDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("{employeeId}")
    public ResponseEntity<EmployeesResponseDto> getEmployeeById(@PathVariable Long employeeId){
        EmployeesResponseDto getEmployee = employeesService.getEmployeeById(employeeId);
        return ResponseEntity.ok(getEmployee);
    }
}
