package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesResponseDto;
import com.example.RatingsApp.service.EmployeesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

    private final EmployeesService employeesService;

    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @PostMapping
    public ResponseEntity<EmployeesResponseDto> createEmployee(@RequestBody EmployeesRequestDto employeesRequestDto) {
        EmployeesResponseDto savedEmployee = employeesService.createEmployee(employeesRequestDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("{employeeId}")
    public ResponseEntity<EmployeesResponseDto> getEmployeeById(@PathVariable Long employeeId){
        EmployeesResponseDto getEmployee = employeesService.getEmployeeById(employeeId);
        return ResponseEntity.ok(getEmployee);
    }

    @PutMapping("{employeeId}")
    public ResponseEntity<EmployeesResponseDto> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeesRequestDto employeesRequestDto){
        EmployeesResponseDto updatedEmployee = employeesService.updateEmployee(employeeId, employeesRequestDto);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EmployeesResponseDto>> getAllEmployees(){
        List<EmployeesResponseDto> listEmployees = employeesService.getAllRoles();
        return ResponseEntity.ok(listEmployees);
    }

    @DeleteMapping("{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId){
        EmployeesResponseDto deletedEmployee = employeesService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee Deleted!");
    }
}
