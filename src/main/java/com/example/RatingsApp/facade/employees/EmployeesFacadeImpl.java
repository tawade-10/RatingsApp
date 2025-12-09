package com.example.RatingsApp.facade.employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.service.Employees.EmployeesService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeesFacadeImpl implements EmployeesFacade{

    private final EmployeesService employeesService;

    public EmployeesFacadeImpl(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @Override
    public EmployeesResponseDto createEmployee(EmployeesRequestDto employeesRequestDto) {
        return employeesService.createEmployee(employeesRequestDto);
    }

    @Override
    public EmployeesResponseDto getEmployeeById(String employeeId) {
        return employeesService.getEmployeeById(employeeId);
    }

    @Override
    public Page<EmployeesResponseDto> getAllEmployees(int page, int size) {
        return employeesService.getAllEmployees(page,size);
    }

    @Override
    public Page<EmployeesResponseDto> getEmployeeByName(int page, int size, String name) {
        return employeesService.getEmployeeByName(page,size,name);
    }

    @Override
    public List<EmployeesResponseDto> getEmployeeByTeam(String teamId) {
        return employeesService.getEmployeeByTeam(teamId);
    }

    @Override
    public List<EmployeesResponseDto> getEmployeesByRole(String roleId) {
        return employeesService.getEmployeesByRole(roleId);
    }

    @Override
    public EmployeesResponseDto getPmByTeam(String teamId) {
        return employeesService.getPmByTeam(teamId);
    }

    @Override
    public List<EmployeesResponseDto> getAllPm() {
        return employeesService.getAllPm();
    }

    @Override
    public EmployeesResponseDto updateEmployee(String employeeId, EmployeesRequestDto employeesRequestDto) {
        return employeesService.updateEmployee(employeeId,employeesRequestDto);
    }

    @Override
    public EmployeesResponseDto deleteEmployee(String employeeId) {
        return employeesService.deleteEmployee(employeeId);
    }

    @Override
    public String verify(EmployeesRequestDto employeesRequestDto) {
        return employeesService.verify(employeesRequestDto);
    }

    @Override
    public EmployeesResponseDto addEmployeeToTeam(EmployeesRequestDto employeesRequestDto, String teamId) {
        return employeesService.addEmployeeToTeam(employeesRequestDto,teamId);
    }

    @Override
    public EmployeesResponseDto changeTeam(String employeeId, EmployeesRequestDto employeesRequestDto) {
        return employeesService.changeTeam(employeeId, employeesRequestDto);
    }

    @Override
    public EmployeesResponseDto changeRole(String employeeId, EmployeesRequestDto employeesRequestDto) {
        return employeesService.changeRole(employeeId, employeesRequestDto);
    }
}
