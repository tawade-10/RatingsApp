package com.example.RatingsApp.service.Employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeesService {
    EmployeesResponseDto createEmployee(EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto getEmployeeById(String empId);

    EmployeesResponseDto updateEmployee(String employeeId, EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto deleteEmployee(String employeeId);

    List<EmployeesResponseDto> getEmployeeByTeam(String teamId);

    List<EmployeesResponseDto> getEmployeesByRole(String roleId);

    EmployeesResponseDto getPmByTeam(String teamId);

    List<EmployeesResponseDto> getAllPm();

    Page<EmployeesResponseDto> getEmployeeByName(int page, int size, String name);

    String verify(EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto addEmployeeToTeam(EmployeesRequestDto employeesRequestDto, String teamId);

    EmployeesResponseDto changeTeam(String employeeId, EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto changeRole(String employeeId, EmployeesRequestDto employeesRequestDto);

    Page<EmployeesResponseDto> getAllEmployees(int page, int size);
}
