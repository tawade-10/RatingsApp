package com.example.RatingsApp.service;

import com.example.RatingsApp.dto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesResponseDto;
import com.example.RatingsApp.entity.Employees;

import java.util.List;
import java.util.Optional;

public interface EmployeesService {
    EmployeesResponseDto createEmployee(EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto getEmployeeById(Long empId);

    List<EmployeesResponseDto> getAllRoles();

    EmployeesResponseDto updateEmployee(Long employeeId, EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto deleteEmployee(Long employeeId);
}
