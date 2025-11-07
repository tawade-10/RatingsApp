//package com.example.RatingsApp.ServiceTest;
//
//import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
//import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
//import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;
//import com.example.RatingsApp.entity.Employees;
//import com.example.RatingsApp.entity.Roles;
//import com.example.RatingsApp.entity.Teams;
//import com.example.RatingsApp.exception.APIException;
//import com.example.RatingsApp.repository.EmployeesRepo;
//import com.example.RatingsApp.repository.RolesRepo;
//import com.example.RatingsApp.repository.TeamsRepo;
//import com.example.RatingsApp.service.Employees.EmployeesServiceImpl;
//import com.example.RatingsApp.service.Teams.TeamsServiceImpl;
//import jakarta.inject.Inject;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class EmployeesControllerTest {
//
//    @Mock
//    private EmployeesRepo employeesRepo;
//
//    @Mock
//    private RolesRepo rolesRepo;
//
//    @Mock
//    private TeamsRepo teamsRepo;
//
//    @InjectMocks
//    private EmployeesServiceImpl employeesService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createEmployeeSuccess() {
//
//        EmployeesRequestDto requestDto = new EmployeesRequestDto();
//        requestDto.setEmployeeId(102L);
//        requestDto.setName("Shubham2");
//        requestDto.setEmail("tawadeshubham2@gmail.com");
//        requestDto.setPassword("Tawade300902");
//        requestDto.setRoleId(2L);
//        requestDto.setTeamId(101L);
//
//        when(employeesRepo.findByName("Shubham2")).thenReturn(Optional.empty());
//        when(employeesRepo.findByEmail("tawadeshubham2@gmail.com")).thenReturn(Optional.empty());
//
//        Roles role = new Roles();
//        role.setRoleId(2L);
//        role.setRoleName("TTL");
//        when(rolesRepo.findById(2L)).thenReturn(Optional.of(role));
//
//        // Mock team
//        Teams team = new Teams();
//        team.setTeamId(101L);
//        team.setTeamName("JAVA");
//        when(teamsRepo.findByTeamId(101L)).thenReturn(Optional.of(team));
//
//        // Mock save result
//        Employees savedEmployee = new Employees();
//        savedEmployee.setEmployeeId(102L);
//        savedEmployee.setName("Shubham2");
//        savedEmployee.setEmail("tawadeshubham2@gmail.com");
//        savedEmployee.setPassword("Tawade300902");
//        savedEmployee.setRole(role);
//        savedEmployee.setTeam(team);
//        when(employeesRepo.save(any(Employees.class))).thenReturn(savedEmployee);
//
//        EmployeesResponseDto response = employeesService.createEmployee(requestDto);
//
//        assertNotNull(response);
//        assertEquals(102L, response.getEmployeeId());
//        assertEquals("Shubham2", response.getName());
//        assertEquals("tawadeshubham2@gmail.com", response.getEmail());
//        assertEquals("TTL", response.getRoleName());
//        assertEquals("JAVA", response.getTeamName());
//        assertEquals(2L, response.getRoleId());
//        assertEquals(101L, response.getTeamId());
//    }
//
//    @Test
//    void createEmployeeAlreadyExistsThrowsException() {
//        EmployeesRequestDto requestDto = new EmployeesRequestDto();
//        requestDto.setEmployeeId(102L);
//        requestDto.setName("Shubham2");
//        requestDto.setEmail("tawadeshubham2@gmail.com");
//        requestDto.setPassword("Tawade300902");
//        requestDto.setRoleId(2L);
//        requestDto.setTeamId(101L);
//
//        Employees existing = new Employees();
//        existing.setEmployeeId(102L);
//        existing.setName("Shubham2");
//        existing.setEmail("tawadeshubham2@gmail.com");
//        existing.setPassword("Tawade300902");
//
//        when(employeesRepo.findByName("Shubham2")).thenReturn(Optional.of(existing));
//        when(employeesRepo.findByEmail("tawadeshubham2@gmail.com")).thenReturn(Optional.of(existing));
//
//        APIException exception = assertThrows(APIException.class,
//                () -> employeesService.createEmployee(requestDto));
//
//        assertEquals("Employee with name 'Shubham2' already exists!", exception.getMessage());
//
//        verify(employeesRepo, never()).save(any(Employees.class));
//    }
//
//    @Test
//    void createEmployeeTeamIdNullThrowsException() {
//
//        EmployeesRequestDto requestDto = new EmployeesRequestDto();
//        requestDto.setEmployeeId(102L);
//        requestDto.setName("Shubham2");
//        requestDto.setEmail("tawadeshubham2@gmail.com");
//        requestDto.setPassword("Tawade300902");
//        requestDto.setRoleId(2L);
//        requestDto.setTeamId(null);
//
//        when(employeesRepo.findByName("Shubham2")).thenReturn(Optional.empty());
//        when(employeesRepo.findByEmail("tawadeshubham2@gmail.com")).thenReturn(Optional.empty());
//
//        Roles role = new Roles();
//        role.setRoleId(2L);
//        role.setRoleName("TTL");
//        when(rolesRepo.findById(2L)).thenReturn(Optional.of(role));
//
//        APIException exception = assertThrows(APIException.class, () -> {
//            employeesService.createEmployee(requestDto);
//        });
//        assertEquals("Team ID must be provided for non-PM employees.", exception.getMessage());
//        verify(employeesRepo, never()).save(any(Employees.class));
//    }
//
//    @Test
//    void getEmployeeByIdSuccess() {
//
//        Roles role = new Roles();
//        role.setRoleId(2L);
//        role.setRoleName("TTL");
//
//        Teams team = new Teams();
//        team.setTeamId(101L);
//
//        Employees employee = new Employees();
//        employee.setEmployeeId(102L);
//        employee.setName("Shubham2");
//        employee.setEmail("tawadeshubham2@gmail.com");
//        employee.setPassword("Tawade300902");
//        employee.setRole(role);
//        employee.setTeam(team);
//
//        when(employeesRepo.findById(102L)).thenReturn(Optional.of(employee));
//
//        EmployeesResponseDto response = employeesService.getEmployeeById(102L);
//
//        assertNotNull(response);
//        assertEquals("Shubham2", response.getName());
//        assertEquals(102L, response.getEmployeeId());
//        assertEquals("TTL", response.getRoleName());
//        assertEquals(101L, response.getTeamId());
//
//        verify(employeesRepo, times(1)).findById(102L);
//    }
//
//
//    @Test
//    void getAllEmployees() {
//    }
//
//    @Test
//    void getEmployeesByName() {
//    }
//
//    @Test
//    void getEmployeesByTeam() {
//    }
//
//    @Test
//    void getEmployeesByRole() {
//    }
//
//    @Test
//    void getPmByTeam() {
//    }
//
//    @Test
//    void getAllPm() {
//    }
//
//    @Test
//    void updateEmployee() {
//    }
//
//    @Test
//    void deleteEmployee() {
//    }
//}