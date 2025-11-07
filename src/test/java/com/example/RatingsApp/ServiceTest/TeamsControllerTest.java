//package com.example.RatingsApp.ServiceTest;
//
//import com.example.RatingsApp.dto.TeamsDto.TeamsRequestDto;
//import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;
//import com.example.RatingsApp.entity.Employees;
//import com.example.RatingsApp.entity.Roles;
//import com.example.RatingsApp.entity.Teams;
//import com.example.RatingsApp.exception.APIException;
//import com.example.RatingsApp.exception.ResourceNotFoundException;
//import com.example.RatingsApp.repository.EmployeesRepo;
//import com.example.RatingsApp.repository.TeamsRepo;
//import com.example.RatingsApp.service.Teams.TeamsServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Sort;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class TeamsControllerTest {
//
//    @Mock
//    private TeamsRepo teamsRepo;
//
//    @Mock
//    private EmployeesRepo employeesRepo;
//
//    @InjectMocks
//    private TeamsServiceImpl teamsService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    // ======================== CREATE TEAM ========================
//    @Test
//    void createTeamSuccess() {
//        TeamsRequestDto requestDto = new TeamsRequestDto(101L, "JAVA", 1L);
//
//        when(teamsRepo.findByTeamNameIgnoreCase("JAVA")).thenReturn(Optional.empty());
//
//        Roles pmRole = new Roles();
//        pmRole.setRoleId(1L);
//        pmRole.setRoleName("PM");
//
//        Employees pm = new Employees();
//        pm.setEmployeeId(1L);
//        pm.setName("Shubham");
//        pm.setRole(pmRole);
//        pm.setManagedTeam(null);
//
//        when(employeesRepo.findByEmployeeId(1L)).thenReturn(Optional.of(pm));
//
//        Teams savedTeam = new Teams();
//        savedTeam.setTeamId(101L);
//        savedTeam.setTeamName("JAVA");
//        savedTeam.setPm(pm);
//
//        when(teamsRepo.save(any(Teams.class))).thenReturn(savedTeam);
//        when(employeesRepo.save(any(Employees.class))).thenReturn(pm);
//
//        TeamsResponseDto response = teamsService.createTeam(requestDto);
//
//        assertNotNull(response);
//        assertEquals(101L, response.getTeamId());
//        assertEquals("JAVA", response.getTeamName());
//        assertEquals(1L, response.getPmId());
//
//        verify(teamsRepo, times(1)).findByTeamNameIgnoreCase("JAVA");
//        verify(teamsRepo, times(1)).save(any(Teams.class));
//        verify(employeesRepo, times(1)).save(any(Employees.class));
//    }
//
//    @Test
//    void createTeamAlreadyExistsThrowsException() {
//        TeamsRequestDto requestDto = new TeamsRequestDto();
//        requestDto.setTeamName("JAVA");
//
//        Teams existing = new Teams();
//        existing.setTeamName("JAVA");
//
//        when(teamsRepo.findByTeamNameIgnoreCase("JAVA")).thenReturn(Optional.of(existing));
//
//        APIException ex = assertThrows(APIException.class, () -> teamsService.createTeam(requestDto));
//        assertEquals("Team name 'JAVA' already exists!", ex.getMessage());
//
//        verify(teamsRepo, times(1)).findByTeamNameIgnoreCase("JAVA");
//        verify(teamsRepo, never()).save(any());
//    }
//
//    @Test
//    void createTeamWithNullPmThrowsException() {
//        TeamsRequestDto requestDto = new TeamsRequestDto();
//        requestDto.setTeamName("JAVA");
//        requestDto.setPmId(null);
//
//        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> teamsService.createTeam(requestDto));
//        assertEquals("Role name cannot be null or empty", ex.getMessage());
//    }
//
//    @Test
//    void createTeamWithPmAlreadyManagingAnotherTeamThrowsException() {
//        TeamsRequestDto requestDto = new TeamsRequestDto(101L, "Java Team", 1L);
//
//        Roles pmRole = new Roles();
//        pmRole.setRoleId(1L);
//
//        Teams existingTeam = new Teams();
//        existingTeam.setTeamId(2L);
//        existingTeam.setTeamName("Python Team");
//
//        Employees pm = new Employees();
//        pm.setEmployeeId(1L);
//        pm.setName("Shubham");
//        pm.setRole(pmRole);
//        pm.setManagedTeam(existingTeam);
//
//        when(employeesRepo.findByEmployeeId(1L)).thenReturn(Optional.of(pm));
//
//        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> teamsService.createTeam(requestDto));
//        assertEquals("The PM 'Shubham' is already managing another team.", ex.getMessage());
//
//        verify(employeesRepo, times(1)).findByEmployeeId(1L);
//        verify(teamsRepo, never()).save(any());
//    }
//
//    // ======================== ASSIGN PM ========================
//    @Test
//    void assignPmSuccess() {
//        TeamsRequestDto requestDto = new TeamsRequestDto();
//        requestDto.setPmId(101L);
//
//        Roles pmRole = new Roles();
//        pmRole.setRoleId(1L);
//
//        Teams existingTeam = new Teams();
//        existingTeam.setTeamId(101L);
//        existingTeam.setTeamName("Java Team");
//
//        Employees pm = new Employees();
//        pm.setEmployeeId(101L);
//        pm.setRole(pmRole);
//
//        when(teamsRepo.findById(101L)).thenReturn(Optional.of(existingTeam));
//        when(employeesRepo.findById(101L)).thenReturn(Optional.of(pm));
//
//        // Save mocks just return argument
//        when(teamsRepo.save(any(Teams.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(employeesRepo.save(any(Employees.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        TeamsResponseDto response = teamsService.assignPm(101L, requestDto);
//
//        assertNotNull(response);
//        assertEquals(101L, response.getTeamId());
//        assertEquals("Java Team", response.getTeamName());
//        assertEquals(101L, response.getPmId());
//
//        verify(teamsRepo, times(1)).findById(101L);
//        verify(employeesRepo, times(1)).findById(101L);
//        verify(teamsRepo, times(2)).save(any(Teams.class)); // 2 saves in service
//        verify(employeesRepo, times(1)).save(any(Employees.class));
//    }
//
//    @Test
//    void assignPmWithInvalidRoleThrowsException() {
//        TeamsRequestDto requestDto = new TeamsRequestDto();
//        requestDto.setPmId(3L);
//
//        Roles ttlRole = new Roles();
//        ttlRole.setRoleId(2L);
//
//        Teams existingTeam = new Teams();
//        existingTeam.setTeamId(1L);
//        existingTeam.setTeamName("Java Team");
//
//        Employees ttl = new Employees();
//        ttl.setEmployeeId(3L);
//        ttl.setRole(ttlRole);
//
//        when(teamsRepo.findById(1L)).thenReturn(Optional.of(existingTeam));
//        when(employeesRepo.findById(3L)).thenReturn(Optional.of(ttl));
//
//        APIException ex = assertThrows(APIException.class, () -> teamsService.assignPm(1L, requestDto));
//        assertEquals("The employee should have role id 1 for being PM", ex.getMessage());
//
//        verify(teamsRepo, times(1)).findById(1L);
//        verify(employeesRepo, times(1)).findById(3L);
//        verify(teamsRepo, never()).save(any());
//    }
//
//    // ======================== GET TEAMS ========================
//    @Test
//    void getAllTeamsListSuccess() {
//        Employees emp1 = new Employees();
//        emp1.setEmployeeId(101L);
//
//        Teams team1 = new Teams();
//        team1.setTeamId(101L);
//        team1.setTeamName("JAVA");
//        team1.setPm(emp1);
//
//        Employees emp2 = new Employees();
//        emp2.setEmployeeId(102L);
//
//        Teams team2 = new Teams();
//        team2.setTeamId(102L);
//        team2.setTeamName("PYTHON");
//        team2.setPm(emp2);
//
//        when(teamsRepo.findAll(Sort.by(Sort.Direction.ASC, "teamId"))).thenReturn(List.of(team1, team2));
//
//        List<TeamsResponseDto> teamsList = teamsService.getAllTeams();
//
//        assertNotNull(teamsList);
//        assertEquals(2, teamsList.size());
//        assertEquals("JAVA", teamsList.get(0).getTeamName());
//        assertEquals("PYTHON", teamsList.get(1).getTeamName());
//
//        verify(teamsRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "teamId"));
//    }
//
//    @Test
//    void getAllTeamsEmptyList() {
//        when(teamsRepo.findAll(Sort.by(Sort.Direction.ASC, "teamId"))).thenReturn(List.of());
//
//        List<TeamsResponseDto> teamsList = teamsService.getAllTeams();
//
//        assertNotNull(teamsList);
//        assertTrue(teamsList.isEmpty());
//        verify(teamsRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "teamId"));
//    }
//
//    @Test
//    void getTeamByIdSuccess() {
//        Teams savedTeam = new Teams();
//        savedTeam.setTeamId(1L);
//        savedTeam.setTeamName("JAVA");
//
//        when(teamsRepo.findById(1L)).thenReturn(Optional.of(savedTeam));
//
//        TeamsResponseDto response = teamsService.getTeamById(1L);
//
//        assertEquals("JAVA", response.getTeamName());
//        verify(teamsRepo, times(1)).findById(1L);
//    }
//
//    @Test
//    void getTeamByIdNotFoundThrowsException() {
//        when(teamsRepo.findById(1L)).thenReturn(Optional.empty());
//
//        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> teamsService.getTeamById(1L));
//        verify(teamsRepo, times(1)).findById(1L);
//    }
//
//    // ======================== UPDATE TEAM ========================
//    @Test
//    void updateTeamSuccess() {
//
//        TeamsRequestDto requestDto = new TeamsRequestDto();
//        requestDto.setTeamId(201L);
//        requestDto.setTeamName("Java Team");
//        requestDto.setPmId(105L);
//
//        Roles pmRole = new Roles();
//        pmRole.setRoleId(1L);
//
//        Employees newPm = new Employees();
//        newPm.setEmployeeId(105L);
//        newPm.setRole(pmRole);
//
//        Teams existingTeam = new Teams();
//        existingTeam.setTeamId(101L);
//        existingTeam.setTeamName("JAVA");
//        existingTeam.setPm(new Employees());
//
//        Teams updatedTeam = new Teams();
//        updatedTeam.setTeamId(201L);
//        updatedTeam.setTeamName("Java Team");
//        updatedTeam.setPm(newPm);
//
//        when(teamsRepo.findById(101L)).thenReturn(Optional.of(existingTeam));
//        when(employeesRepo.findById(105L)).thenReturn(Optional.of(newPm));
//        when(teamsRepo.save(any(Teams.class))).thenReturn(updatedTeam);
//
//        TeamsResponseDto response = teamsService.updateTeam(101L, requestDto);
//
//        assertNotNull(response);
//        assertEquals(201L, response.getTeamId());
//        assertEquals("Java Team", response.getTeamName());
//        assertEquals(105L, response.getPmId());
//
//        verify(teamsRepo, times(1)).findById(101L);
//        verify(employeesRepo, times(1)).findById(105L);
//        verify(teamsRepo, times(1)).save(any(Teams.class));
//    }
//
//
//    @Test
//    void updateTeamNotFoundThrowsException() {
//        TeamsRequestDto requestDto = new TeamsRequestDto();
//        requestDto.setTeamName("Java Team");
//
//        when(teamsRepo.findByTeamId(101L)).thenReturn(Optional.empty());
//
//        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> teamsService.updateTeam(101L, requestDto));
//        verify(teamsRepo, never()).save(any());
//    }
//}
