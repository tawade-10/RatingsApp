package com.example.RatingsApp.facade.employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface EmployeesFacade {
    EmployeesResponseDto createEmployee(@Valid EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto getEmployeeById(String employeeId);

    List<EmployeesResponseDto> getAllEmployees();

    List<EmployeesResponseDto> getEmployeeByName(String name);

    List<EmployeesResponseDto> getEmployeeByTeam(String teamId);

    List<EmployeesResponseDto> getEmployeesByRole(String roleId);

    EmployeesResponseDto getPmByTeam(String teamId);

    List<EmployeesResponseDto> getAllPm();

    EmployeesResponseDto updateEmployee(String employeeId, EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto deleteEmployee(String employeeId);

    String verify(EmployeesRequestDto employeesRequestDto);
}
