package com.example.RatingsApp.service.Employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.repository.TeamsRepo;
import com.example.RatingsApp.service.Security.JwtService;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    private final EmployeesRepo employeesRepo;

    private final RolesRepo rolesRepo;

    private final TeamsRepo teamsRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public EmployeesServiceImpl(EmployeesRepo employeesRepo, RolesRepo rolesRepo, TeamsRepo teamsRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.employeesRepo = employeesRepo;
        this.rolesRepo = rolesRepo;
        this.teamsRepo = teamsRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public EmployeesResponseDto createEmployee(EmployeesRequestDto employeesRequestDto) {
        Employees employee = new Employees();

        Roles role = rolesRepo.findByRoleIdIgnoreCase(employeesRequestDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + employeesRequestDto.getRoleId()));

        Optional<Employees> existingEmployeeByName = employeesRepo.findByName(employeesRequestDto.getName());
        if (existingEmployeeByName.isPresent()) {
            throw new APIException("Employee with name '" + employeesRequestDto.getName() + "' already exists!");
        }

        Optional<Employees> existingEmployeeByEmail = employeesRepo.findByEmail(employeesRequestDto.getEmail());
        if (existingEmployeeByEmail.isPresent()) {
            throw new APIException("Employee with email '" + employeesRequestDto.getEmail() + "' already exists!");
        }

        Teams team = null;

        if (!"R101".equalsIgnoreCase(role.getRoleId())) {
            if (employeesRequestDto.getTeamId() == null) {
                throw new APIException("Team ID must be provided for non-PM employees.");
            }
            team = teamsRepo.findByTeamIdIgnoreCase(employeesRequestDto.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + employeesRequestDto.getTeamId()));
        }

        employee.setEmployeeId(employeesRequestDto.getEmployeeId());
        employee.setName(employeesRequestDto.getName());
        employee.setEmail(employeesRequestDto.getEmail());
        employee.setPassword(passwordEncoder.encode(employeesRequestDto.getPassword()));
        employee.setRole(role);
        employee.setTeam(team);
        Employees saveEmployee = employeesRepo.save(employee);

        return new EmployeesResponseDto(saveEmployee);
    }

    @Override
    public EmployeesResponseDto getEmployeeById(String employeeId) {
        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(employeeId)
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
    public String verify(EmployeesRequestDto employeesRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        employeesRequestDto.getEmail(),
                        employeesRequestDto.getPassword())
        );
        if (authentication.isAuthenticated()) {
            Employees user = employeesRepo.findByEmail(employeesRequestDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return jwtService.generateToken(employeesRequestDto.getEmail(), employeesRequestDto.getRoleId());
        }
        return "Fail";
    }

    @Override
    public EmployeesResponseDto updateEmployee(String employeeId, EmployeesRequestDto employeesRequestDto) {

        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Roles role = rolesRepo.findByRoleIdIgnoreCase(employeesRequestDto.getRoleId())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + employeesRequestDto.getRoleId()));

        Teams team = teamsRepo.findByTeamIdIgnoreCase(employeesRequestDto.getTeamId())
                        .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + employeesRequestDto.getTeamId()));

        employee.setEmployeeId(employeesRequestDto.getEmployeeId());
        employee.setName(employeesRequestDto.getName());
        employee.setEmail(employeesRequestDto.getEmail());
        employee.setPassword(employeesRequestDto.getPassword());
        employee.setRole(role);
        employee.setTeam(team);
        Employees saveUpdatedEmployee = employeesRepo.save(employee);
        return new EmployeesResponseDto(saveUpdatedEmployee);
    }

    @Override
    public EmployeesResponseDto deleteEmployee(String employeeId) {
        Employees employee = employeesRepo.findByEmployeeIdIgnoreCase(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        employeesRepo.delete(employee);
        return null;
    }

    @Override
    public List<EmployeesResponseDto> getEmployeeByTeam(String teamId) {
        Teams team = teamsRepo.findByTeamIdIgnoreCase(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        List<Employees> employees = employeesRepo.findByTeam(team);

        return employees.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<EmployeesResponseDto> getEmployeesByRole(String roleId) {
        Roles role = rolesRepo.findByRoleIdIgnoreCase(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        List<Employees> employees = employeesRepo.findByRole(role);

        return employees.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public EmployeesResponseDto getPmByTeam(String teamId) {
        Teams team = teamsRepo.findByTeamNameIgnoreCase(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        String pmId = team.getPm().getEmployeeId();
        if (pmId == null) {
            throw new IllegalArgumentException("PM ID must be provided in the request body.");
        }
        Employees pm = employeesRepo.findByEmployeeIdIgnoreCase(pmId).orElseThrow(
                () -> new ResourceNotFoundException("Employee (PM) not found with ID: " + pmId)
        );
        return new EmployeesResponseDto(pm);
    }

    @Override
    public List<EmployeesResponseDto> getAllPm() {
        List<Employees> employees = employeesRepo.findAll(Sort.by(Sort.Direction.ASC, "employeeId"));
        List<Employees> getPmList = employees.stream().filter(emp -> "R101".equalsIgnoreCase(emp.getRole().getRoleId())).collect(Collectors.toList());;
        return getPmList.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }
}
