package com.example.RatingsApp.service.Employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;

import java.util.List;

public interface EmployeesService {
    EmployeesResponseDto createEmployee(EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto getEmployeeById(Long empId);

    EmployeesResponseDto updateEmployee(Long employeeId, EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto deleteEmployee(Long employeeId);

    List<EmployeesResponseDto> getEmployeeByTeam(Long teamId);

    List<EmployeesResponseDto> getEmployeesByRole(Long roleId);

    EmployeesResponseDto getPmByTeam(Long teamId);

    List<EmployeesResponseDto> getAllPm();

    List<EmployeesResponseDto> getAllEmployees();

    List<EmployeesResponseDto> getEmployeeByName(String name);
}
