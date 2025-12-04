package com.example.RatingsApp.service.Employees;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
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

import java.util.List;
import java.util.Optional;
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

        Roles role = rolesRepo.findById(employeesRequestDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + employeesRequestDto.getRoleId()));

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
    public EmployeesResponseDto getEmployeeById(Long employeeId) {
        Employees employee = employeesRepo.findById(employeeId)
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
    public EmployeesResponseDto addEmployeeToTeam(EmployeesRequestDto employeesRequestDto, Long teamId) {

        Employees employee = employeesRepo.findById(employeesRequestDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeesRequestDto.getEmployeeId()));

        Teams team = teamsRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        if (employee.getTeam() != null) {
            return new EmployeesResponseDto(employee);
        }

        employee.setTeam(team);

        if (employee.getRole().getRoleId() == 2L) {
            team.setPm(employee);
            teamsRepo.save(team);
        }

        employee.setTeam(team);
        Employees saveUpdatedEmployee =  employeesRepo.save(employee);
        return new EmployeesResponseDto(saveUpdatedEmployee);
    }

    @Override
    public EmployeesResponseDto changeTeam(Long employeeId, EmployeesRequestDto employeesRequestDto) {

        Long teamId = employeesRequestDto.getTeamId();

        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Teams newTeam = teamsRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        employee.setTeam(newTeam);
        Employees updatedEmployee = employeesRepo.save(employee);

        return new EmployeesResponseDto(updatedEmployee);
    }

    @Override
    public EmployeesResponseDto changeRole(EmployeesRequestDto employeesRequestDto, Long employeeId, Long roleId) {

        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Roles newRole = rolesRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

        employee.setRole(newRole);
        Employees updatedEmployee = employeesRepo.save(employee);

        return new EmployeesResponseDto(updatedEmployee);
    }

    @Override
    public EmployeesResponseDto updateEmployee(Long employeeId, EmployeesRequestDto employeesRequestDto) {

        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        employee.setName(employeesRequestDto.getName());
        employee.setEmail(employeesRequestDto.getEmail());
        employee.setPassword(employeesRequestDto.getPassword());
        Employees saveUpdatedEmployee = employeesRepo.save(employee);
        return new EmployeesResponseDto(saveUpdatedEmployee);
    }

    @Override
    public EmployeesResponseDto deleteEmployee(Long employeeId) {
        Employees employee = employeesRepo.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        employeesRepo.delete(employee);
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

        Roles pmRole = rolesRepo.findByRoleNameIgnoreCase("PM")
                .orElseThrow(() -> new ResourceNotFoundException("PM Role not found"));

        Long pmRoleId = pmRole.getRoleId();

        List<Employees> employees = employeesRepo.findAll(Sort.by(Sort.Direction.ASC, "employeeId"));

        List<Employees> getPmList = employees.stream()
                        .filter(emp -> pmRoleId.equals(emp.getRole().getRoleId()))
                        .collect(Collectors.toList());

        return getPmList.stream().map(EmployeesResponseDto::new).collect(Collectors.toList());
    }

}
