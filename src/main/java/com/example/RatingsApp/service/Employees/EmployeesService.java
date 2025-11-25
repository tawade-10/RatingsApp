package com.example.RatingsApp.service.Employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import org.springframework.data.domain.Page;

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

    Page<EmployeesResponseDto> getEmployeeByName(int page, int size, String name);

    String verify(EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto addEmployeeToTeam(EmployeesRequestDto employeesRequestDto, Long teamId);

    EmployeesResponseDto changeTeam(EmployeesRequestDto employeesRequestDto, Long employeeId);

    EmployeesResponseDto changeRole(EmployeesRequestDto employeesRequestDto, Long employeeId, Long roleId);

    Page<EmployeesResponseDto> getAllEmployees(int page, int size);
}
