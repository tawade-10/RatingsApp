package com.example.RatingsApp.serviceImplementation;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsRequestDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.repository.TeamsRepo;
import com.example.RatingsApp.service.EmployeesService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Optional<Employees> existingEmployeeByName = employeesRepo.findByName(employeesRequestDto.getName());
        if (existingEmployeeByName.isPresent()) {
            throw new APIException("Employee with name '" + employeesRequestDto.getName() + "' already exists!");
        }

        Optional<Employees> existingEmployeeByEmail = employeesRepo.findByEmail(employeesRequestDto.getEmail());
        if (existingEmployeeByEmail.isPresent()) {
            throw new APIException("Employee with email '" + employeesRequestDto.getEmail() + "' already exists!");
        }

        Roles role = rolesRepo.findById(employeesRequestDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + employeesRequestDto.getRoleId()));

        Teams team = teamsRepo.findById(employeesRequestDto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + employeesRequestDto.getTeamId()));

        if (role.getRoleId() == 1L) {
            if (team.getPm() != null) {
                throw new APIException("This team already has a PM assigned (" + team.getPm().getName() + ").");
            }
        }

        employee.setName(employeesRequestDto.getName());
        employee.setEmail(employeesRequestDto.getEmail());
        employee.setPassword(employeesRequestDto.getPassword());
        employee.setRole(role);
        employee.setTeam(team);
        Employees saveEmployee = employeesRepo.save(employee);

        if (role.getRoleId() == 1L) {
            team.setPm(saveEmployee);
            teamsRepo.save(team);
        }

        return new EmployeesResponseDto(saveEmployee);
    }

    @Override
    public EmployeesResponseDto getEmployeeById(Long employeeId) {
        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        return new EmployeesResponseDto(employee);
    }

    @Override
    public List<EmployeesResponseDto> getAllEmployees() {
        List<Employees> employees = employeesRepo.findAll(Sort.by(Sort.Direction.ASC, "employeeId"));
        return employees.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<EmployeesResponseDto> getEmployeeByName(String name) {
        List<Employees> listEmployees = employeesRepo.findAll(Sort.by(Sort.Direction.ASC, "employeeId"));
        List<Employees> getEmployeesByName = listEmployees.stream().filter(emp -> emp.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        return getEmployeesByName.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public EmployeesResponseDto updateEmployee(Long employeeId, EmployeesRequestDto employeesRequestDto) {
        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Roles role = rolesRepo.findById(employeesRequestDto.getRoleId())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + employeesRequestDto.getRoleId()));

        Teams team = teamsRepo.findById(employeesRequestDto.getTeamId())
                        .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + employeesRequestDto.getTeamId()));

        employee.setName(employeesRequestDto.getName());
        employee.setEmail(employeesRequestDto.getEmail());
        employee.setPassword(employeesRequestDto.getPassword());
        employee.setRole(role);
        employee.setTeam(team);
        Employees saveUpdatedEmployee = employeesRepo.save(employee);
        return new EmployeesResponseDto(saveUpdatedEmployee);
    }

    @Override
    public EmployeesResponseDto deleteEmployee(Long employeeId) {
        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        employeesRepo.deleteById(employeeId);
        return null;
    }

    @Override
    public List<EmployeesResponseDto> getEmployeeByTeam(Long teamId) {
        Teams team = teamsRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        List<Employees> employees = employeesRepo.findByTeam(team);

        return employees.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<EmployeesResponseDto> getEmployeesByRole(Long roleId) {
        Roles role = rolesRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        List<Employees> employees = employeesRepo.findByRole(role);

        return employees.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public EmployeesResponseDto getPmByTeam(Long teamId) {
        Teams team = teamsRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        Long pmId = team.getPm().getEmployeeId();
        if (pmId == null) {
            throw new IllegalArgumentException("PM ID must be provided in the request body.");
        }
        Employees pm = employeesRepo.findById(pmId).orElseThrow(
                () -> new ResourceNotFoundException("Employee (PM) not found with ID: " + pmId)
        );
        return new EmployeesResponseDto(pm);
    }

    @Override
    public List<EmployeesResponseDto> getAllPm() {
        List<Employees> employees = employeesRepo.findAll(Sort.by(Sort.Direction.ASC, "employeeId"));
        List<Employees> getPmList = employees.stream().filter(emp -> emp.getRole().getRoleId() == 1).collect(Collectors.toList());
        return getPmList.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }
}
