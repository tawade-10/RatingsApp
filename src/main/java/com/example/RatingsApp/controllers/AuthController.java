package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.facade.employees.EmployeesFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final EmployeesFacade employeesFacade;

    public AuthController(EmployeesFacade employeesFacade) {
        this.employeesFacade = employeesFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<EmployeesResponseDto> registerEmployee(@Valid @RequestBody EmployeesRequestDto employeesRequestDto) {
        EmployeesResponseDto registeredEmployee = employeesFacade.createEmployee(employeesRequestDto);
        return new ResponseEntity<>(registeredEmployee, HttpStatus.CREATED);
    }
}
