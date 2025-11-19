package com.example.RatingsApp.ServiceTest;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.repository.TeamsRepo;
import com.example.RatingsApp.service.Employees.EmployeesServiceImpl;
import com.example.RatingsApp.service.Teams.TeamsServiceImpl;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeesControllerTest {

    @Mock
    private EmployeesRepo employeesRepo;

    @Mock
    private RolesRepo rolesRepo;

    @Mock
    private TeamsRepo teamsRepo;

    @InjectMocks
    private EmployeesServiceImpl employeesService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEmployeeSuccess() {

        EmployeesRequestDto requestDto = new EmployeesRequestDto();
        requestDto.setEmployeeId("EMP102");
        requestDto.setName("Shubham2");
        requestDto.setEmail("tawadeshubham2@gmail.com");
        requestDto.setPassword("Tawade300902");
        requestDto.setRoleId("R102");
        requestDto.setTeamId("T101");

        when(employeesRepo.findByName("Shubham2")).thenReturn(Optional.empty());
        when(employeesRepo.findByEmail("tawadeshubham2@gmail.com")).thenReturn(Optional.empty());

        Roles role = new Roles();
        role.setRoleId("R102");
        role.setRoleName("TL");
        when(rolesRepo.findByRoleIdIgnoreCase("R102")).thenReturn(Optional.of(role));

        Teams team = new Teams();
        team.setTeamId("T101");
        team.setTeamName("JAVA");
        when(teamsRepo.findByTeamIdIgnoreCase("T101")).thenReturn(Optional.of(team));

        when(passwordEncoder.encode("Tawade300902")).thenReturn("encodedPassword");

        Employees savedEmployee = new Employees();
        savedEmployee.setEmployeeId("EMP102");
        savedEmployee.setName("Shubham2");
        savedEmployee.setEmail("tawadeshubham2@gmail.com");
        savedEmployee.setPassword("encodedPassword");
        savedEmployee.setRole(role);
        savedEmployee.setTeam(team);
        when(employeesRepo.save(any(Employees.class))).thenReturn(savedEmployee);

        EmployeesResponseDto response = employeesService.createEmployee(requestDto);

        assertNotNull(response);
        assertEquals("EMP102", response.getEmployeeId());
        assertEquals("Shubham2", response.getName());
        assertEquals("tawadeshubham2@gmail.com", response.getEmail());
        assertEquals("R102", response.getRoleId());
        assertEquals("T101", response.getTeamId());
        assertEquals("TL", response.getRoleName());
        assertEquals("JAVA", response.getTeamName());
    }

    @Test
    void createEmployeeAlreadyExistsThrowsException() {

        EmployeesRequestDto requestDto = new EmployeesRequestDto();
        requestDto.setEmployeeId("EMP102");
        requestDto.setName("Shubham2");
        requestDto.setEmail("tawadeshubham2@gmail.com");
        requestDto.setPassword("Tawade300902");
        requestDto.setRoleId("R102");
        requestDto.setTeamId("T101");

        Roles role = new Roles();
        role.setRoleId("R102");
        role.setRoleName("TL");
        when(rolesRepo.findByRoleIdIgnoreCase("R102")).thenReturn(Optional.of(role));

        when(employeesRepo.findByName("Shubham2")).thenReturn(Optional.of(new Employees()));

        APIException exception = assertThrows(APIException.class,
                () -> employeesService.createEmployee(requestDto));

        assertEquals("Employee with name 'Shubham2' already exists!",
                exception.getMessage());
        verify(employeesRepo, never()).save(any(Employees.class));
    }

    @Test
    void createEmployeeTeamIdNullThrowsException() {

        EmployeesRequestDto requestDto = new EmployeesRequestDto();
        requestDto.setEmployeeId("EMP102");
        requestDto.setName("Shubham2");
        requestDto.setEmail("tawadeshubham2@gmail.com");
        requestDto.setPassword("Tawade300902");
        requestDto.setRoleId("R102");
        requestDto.setTeamId(null);

        when(employeesRepo.findByName("Shubham2")).thenReturn(Optional.empty());
        when(employeesRepo.findByEmail("tawadeshubham2@gmail.com")).thenReturn(Optional.empty());

        Roles role = new Roles();
        role.setRoleId("R102");
        role.setRoleName("TL");
        when(rolesRepo.findByRoleIdIgnoreCase("R102")).thenReturn(Optional.of(role));

        APIException exception = assertThrows(APIException.class, () -> {
            employeesService.createEmployee(requestDto);
        });
        assertEquals("Team ID must be provided for non-PM employees.", exception.getMessage());
        verify(employeesRepo, never()).save(any(Employees.class));
    }

    @Test
    void getEmployeeByIdSuccess() {

        Roles role = new Roles();
        role.setRoleId("R102");
        role.setRoleName("TL");

        Teams team = new Teams();
        team.setTeamId("T101");
        team.setTeamName("JAVA");

        Employees employee = new Employees();
        employee.setEmployeeId("EMP102");
        employee.setName("Shubham2");
        employee.setEmail("tawadeshubham2@gmail.com");
        employee.setPassword("Tawade300902");
        employee.setRole(role);
        employee.setTeam(team);

        when(employeesRepo.findByEmployeeIdIgnoreCase("EMP102")).thenReturn(Optional.of(employee));

        EmployeesResponseDto response = employeesService.getEmployeeById("EMP102");

        assertNotNull(response);
        assertEquals("Shubham2", response.getName());
        assertEquals("EMP102", response.getEmployeeId());
        assertEquals("TL", response.getRoleName());
        assertEquals("T101", response.getTeamId());

        verify(employeesRepo, times(1)).findByEmployeeIdIgnoreCase("EMP102");
    }


    @Test
    void getAllEmployees() {
    }

    @Test
    void getEmployeesByName() {
    }

    @Test
    void getEmployeesByTeam() {
    }

    @Test
    void getEmployeesByRole() {
    }

    @Test
    void getPmByTeam() {
    }

    @Test
    void getAllPm() {
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleteEmployee() {
    }
}