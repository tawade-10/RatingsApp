package com.example.RatingsApp.service.Employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.dto.RatingsDto.RatingsResponseDto;
import com.example.RatingsApp.entity.*;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RatingsRepo;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.repository.TeamsRepo;
import com.example.RatingsApp.service.Security.JwtService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    private final EmployeesRepo employeesRepo;

    private final RolesRepo rolesRepo;

    private final TeamsRepo teamsRepo;

    private final RatingsRepo ratingsRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public EmployeesServiceImpl(EmployeesRepo employeesRepo, RolesRepo rolesRepo, TeamsRepo teamsRepo, RatingsRepo ratingsRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.employeesRepo = employeesRepo;
        this.rolesRepo = rolesRepo;
        this.teamsRepo = teamsRepo;
        this.ratingsRepo = ratingsRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public EmployeesResponseDto createEmployee(EmployeesRequestDto employeesRequestDto) {

        Employees employee = new Employees();

        Roles role = rolesRepo.findByRoleId(employeesRequestDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + employeesRequestDto.getRoleId()));

        if(Objects.equals(role.getRoleId(), "ROL001")){
            throw new APIException("Admin role cannot be assigned to anyone!");
        }

        Optional<Employees> existingEmployeeByName = employeesRepo.findByName(employeesRequestDto.getName());
        if (existingEmployeeByName.isPresent()) {
            return new EmployeesResponseDto(existingEmployeeByName.get());
        }

        Optional<Employees> existingEmployeeByEmail = employeesRepo.findByEmail(employeesRequestDto.getEmail());
        if (existingEmployeeByEmail.isPresent()) {
            return new EmployeesResponseDto(existingEmployeeByEmail.get());
        }

//      employee.setEmployeeId(employeesRequestDto.getEmployeeId());
        employee.setName(employeesRequestDto.getName());
        employee.setEmail(employeesRequestDto.getEmail());
        employee.setPassword(passwordEncoder.encode(employeesRequestDto.getPassword()));
        employee.setRole(role);
        employee.setTeam(null);
        Employees saveEmployee = employeesRepo.save(employee);

        return new EmployeesResponseDto(saveEmployee);
    }

    @Override
    public EmployeesResponseDto getEmployeeById(String employeeId) {
        Employees employee = employeesRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        return new EmployeesResponseDto(employee);
    }

    @Override
    public Page<EmployeesResponseDto> getAllEmployees(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Employees> employeesPage = employeesRepo.findAll(pageable);

        return employeesPage.map(EmployeesResponseDto::new);
    }

    @Override
    public Page<EmployeesResponseDto> getEmployeeByName(int page, int size, String name) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Employees> employeesPage = employeesRepo.findByName(name, pageable);

        return employeesPage.map(EmployeesResponseDto::new);
    }


    @Override
    public String verify(EmployeesRequestDto employeesRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        employeesRequestDto.getEmail(),
                        employeesRequestDto.getPassword()
                )
        );

        if (!authentication.isAuthenticated()) {
            return "Invalid Credentials!";
        }

        Employees user = (Employees) authentication.getPrincipal();

        return jwtService.generateToken(user);
    }

    @Override
    public EmployeesResponseDto addEmployeeToTeam(EmployeesRequestDto employeesRequestDto, String teamId) {

        Employees employee = employeesRepo.findByEmployeeId(employeesRequestDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeesRequestDto.getEmployeeId()));

        Teams team = teamsRepo.findByTeamId(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        if (employee.getTeam() != null) {
            return new EmployeesResponseDto(employee);
        }

        if (Objects.equals(employee.getRole().getRoleId(), "ROL001")) {
            throw new APIException("Admin cannot be assigned to a Team");
        }

        if (Objects.equals(employee.getRole().getRoleId(), "ROL002")) {

            if (team.getPm() != null) {
                return new EmployeesResponseDto(team.getPm());
            }

            team.setPm(employee);
            employee.setTeam(team);

            employeesRepo.save(employee);
            teamsRepo.save(team);

            return new EmployeesResponseDto(employee);
        }

        employee.setTeam(team);
        Employees updated = employeesRepo.save(employee);

        return new EmployeesResponseDto(updated);
    }

    @Override
    public EmployeesResponseDto changeTeam(String employeeId, EmployeesRequestDto employeesRequestDto) {

        String teamId = employeesRequestDto.getTeamId();

        Employees employee = employeesRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Teams newTeam = teamsRepo.findByTeamId(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        employee.setTeam(newTeam);
        Employees updatedEmployee = employeesRepo.save(employee);

        return new EmployeesResponseDto(updatedEmployee);
    }

    @Override
    public EmployeesResponseDto changeRole(String employeeId, EmployeesRequestDto employeesRequestDto) {

        String roleId = employeesRequestDto.getRoleId();

        Employees employee = employeesRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Roles newRole = rolesRepo.findByRoleId(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        employee.setRole(newRole);
        Employees updatedEmployee = employeesRepo.save(employee);

        return new EmployeesResponseDto(updatedEmployee);
    }

    @Override
    public EmployeesResponseDto updateEmployee(String employeeId, EmployeesRequestDto employeesRequestDto) {

        Employees employee = employeesRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        employee.setName(employeesRequestDto.getName());
        employee.setEmail(employeesRequestDto.getEmail());
        employee.setPassword(employeesRequestDto.getPassword());
        Employees saveUpdatedEmployee = employeesRepo.save(employee);
        return new EmployeesResponseDto(saveUpdatedEmployee);
    }

    @Override
    public EmployeesResponseDto deleteEmployee(String employeeId) {
        Employees employee = employeesRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        employeesRepo.delete(employee);
        return null;
    }

    @Override
    public List<EmployeesResponseDto> getEmployeeByTeam(String teamId) {
        Teams team = teamsRepo.findByTeamId(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        List<Employees> employees = employeesRepo.findByTeam(team);

        return employees.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<EmployeesResponseDto> getEmployeesByRole(String roleId) {
        Roles role = rolesRepo.findByRoleId(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        List<Employees> employees = employeesRepo.findByRole(role);

        return employees.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public EmployeesResponseDto getPmByTeam(String teamId) {
        Teams team = teamsRepo.findByTeamId(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        String pmId = team.getPm().getEmployeeId();
        if (pmId == null) {
            throw new IllegalArgumentException("PM ID must be provided in the request body.");
        }
        Employees pm = employeesRepo.findByEmployeeId(pmId).orElseThrow(
                () -> new ResourceNotFoundException("Employee (PM) not found with ID: " + pmId)
        );
        return new EmployeesResponseDto(pm);
    }

    @Override
    public List<EmployeesResponseDto> getAllPm() {

        Roles pmRole = rolesRepo.findByRoleNameIgnoreCase("PM")
                .orElseThrow(() -> new ResourceNotFoundException("PM Role not found"));

        String pmRoleId = pmRole.getRoleId();

        List<Employees> employees = employeesRepo.findAll(Sort.by(Sort.Direction.ASC, "employeeId"));

        List<Employees> getPmList = employees.stream()
                        .filter(emp -> pmRoleId.equals(emp.getRole().getRoleId()))
                        .collect(Collectors.toList());

        return getPmList.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

}
