package com.example.RatingsApp.serviceImplementation;

import com.example.RatingsApp.dto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesResponseDto;
import com.example.RatingsApp.dto.TeamsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.repository.TeamsRepo;
import com.example.RatingsApp.service.EmployeesService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    private final EmployeesRepo employeesRepo;

    private final RolesRepo rolesRepo;

    private final TeamsRepo teamsRepo;

    public EmployeesServiceImpl(EmployeesRepo employeesRepo, RolesRepo rolesRepo, TeamsRepo teamsRepo) {
        this.employeesRepo = employeesRepo;
        this.rolesRepo = rolesRepo;
        this.teamsRepo = teamsRepo;
    }

    @Override
    public EmployeesResponseDto createEmployee(EmployeesRequestDto employeesRequestDto) {
        Employees employee = new Employees();

        Roles role = rolesRepo.findById(employeesRequestDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + employeesRequestDto.getRoleId()));

        Teams team = teamsRepo.findById(employeesRequestDto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + employeesRequestDto.getTeamId()));

        employee.setName(employeesRequestDto.getName());
        employee.setEmail(employeesRequestDto.getEmail());
        employee.setPassword(employeesRequestDto.getPassword());
        employee.setRole(role);
        employee.setTeam(team);
        Employees saveEmployee = employeesRepo.save(employee);
        return new EmployeesResponseDto(saveEmployee);
    }

    @Override
    public EmployeesResponseDto getEmployeeById(Long employeeId) {
        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        return new EmployeesResponseDto(employee);
    }
}
