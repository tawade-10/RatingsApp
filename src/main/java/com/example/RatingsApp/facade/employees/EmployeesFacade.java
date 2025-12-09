package com.example.RatingsApp.facade.employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeesFacade {
    EmployeesResponseDto createEmployee(@Valid EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto getEmployeeById(String employeeId);

    Page<EmployeesResponseDto> getAllEmployees(int page, int size);

    Page<EmployeesResponseDto> getEmployeeByName(int page, int size, String name);

    List<EmployeesResponseDto> getEmployeeByTeam(String teamId);

    List<EmployeesResponseDto> getEmployeesByRole(String roleId);

    EmployeesResponseDto getPmByTeam(String teamId);

    List<EmployeesResponseDto> getAllPm();

    EmployeesResponseDto updateEmployee(String employeeId, EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto deleteEmployee(String employeeId);

    String verify(EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto addEmployeeToTeam(EmployeesRequestDto employeesRequestDto, String teamId);

    EmployeesResponseDto changeTeam(String employeeId, EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto changeRole(String employeeId, EmployeesRequestDto employeesRequestDto);
}
