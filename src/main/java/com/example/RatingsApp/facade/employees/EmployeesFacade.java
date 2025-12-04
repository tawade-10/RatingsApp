package com.example.RatingsApp.facade.employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeesFacade {
    EmployeesResponseDto createEmployee(@Valid EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto getEmployeeById(Long employeeId);

    Page<EmployeesResponseDto> getAllEmployees(int page, int size);

    Page<EmployeesResponseDto> getEmployeeByName(int page, int size, String name);

    List<EmployeesResponseDto> getEmployeeByTeam(Long teamId);

    List<EmployeesResponseDto> getEmployeesByRole(Long roleId);

    EmployeesResponseDto getPmByTeam(Long teamId);

    List<EmployeesResponseDto> getAllPm();

    EmployeesResponseDto updateEmployee(Long employeeId, EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto deleteEmployee(Long employeeId);

    String verify(EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto addEmployeeToTeam(EmployeesRequestDto employeesRequestDto, Long teamId);

    EmployeesResponseDto changeTeam( Long employeeId, EmployeesRequestDto employeesRequestDto);

    EmployeesResponseDto changeRole(EmployeesRequestDto employeesRequestDto, Long employeeId, Long roleId);
}
