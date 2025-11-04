package com.example.RatingsApp.ServiceTest;

import com.example.RatingsApp.dto.RolesDto.RolesRequestDto;
import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsRequestDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.repository.TeamsRepo;
import com.example.RatingsApp.service.Roles.RolesServiceImpl;
import com.example.RatingsApp.service.Teams.TeamsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TeamsControllerTest {

    @Mock
    private TeamsRepo teamsRepo;

    @Mock
    private EmployeesRepo employeesRepo;

    @InjectMocks
    private TeamsServiceImpl teamsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTeamSuccess() {

        //Create a mock object and assign values
        TeamsRequestDto requestDto = new TeamsRequestDto();
        requestDto.setTeamName("JAVA");
        requestDto.setPmId(1L);

        //Check that the Team name does not exist in the Mock repo and to create new by returning empty
        when(teamsRepo.findByTeamNameIgnoreCase("JAVA")).thenReturn(Optional.empty());

        //Mock object for roles to get PM
        Roles pmRole = new Roles();
        pmRole.setRoleId(1L);
        pmRole.setRoleName("PM");

        //Mock object for Employees to get employee with role_id as 1L
        Employees pm = new Employees();
        pm.setEmployeeId(1L);
        pm.setName("Shubham1");
        pm.setRole(pmRole);
        pm.setManagedTeam(null);

        //Check that the employee with role_id 1 is present in mock repo and return the object of employee
        when(employeesRepo.findById(1L)).thenReturn(Optional.of(pm));

        //Assign values to mock Team
        Teams savedTeam = new Teams();
        savedTeam.setTeamId(1L);
        savedTeam.setTeamName("JAVA");
        savedTeam.setPm(pm);

        //Saving in Mock repo
        when(teamsRepo.save(any(Teams.class))).thenReturn(savedTeam);

        TeamsResponseDto response = teamsService.createTeam(requestDto);

        assertNotNull(response);
        assertEquals(1L, response.getTeamId());
        assertEquals("JAVA", response.getTeamName());
        assertEquals(1L, response.getPmId());
    }

    @Test
    void createTeamAlreadyExistsThrowsException() {

        TeamsRequestDto requestDto = new TeamsRequestDto();
        requestDto.setTeamName("JAVA");

        Teams existing = new Teams();
        existing.setTeamName("JAVA");

        when(teamsRepo.findByTeamNameIgnoreCase("JAVA")).thenReturn(Optional.of(existing));

        APIException ex = assertThrows(APIException.class, () -> {
            teamsService.createTeam(requestDto);
        });

        assertEquals("Team name 'JAVA' already exists!", ex.getMessage());
        verify(teamsRepo, times(1)).findByTeamNameIgnoreCase("JAVA");
        verify(teamsRepo, never()).save(any(Teams.class));
    }

    @Test
    void createTeamNotNullThrowsException(){
        TeamsRequestDto requestDto = new TeamsRequestDto();
        requestDto.setTeamName(null);

        assertThrows(IllegalArgumentException.class, () -> {
            teamsService.createTeam(requestDto);
        });
    }

    @Test
    void createTeamWithInvalidPmThrowsException(){
        TeamsRequestDto requestDto = new TeamsRequestDto();
        requestDto.setPmId(4L);

        when(employeesRepo.findById(4L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            teamsService.createTeam(requestDto);
        });

        assertEquals("Employee found with ID: 4 is not a PM", ex.getMessage());

        verify(employeesRepo, times(1)).findById(4L);
        verify(teamsRepo, never()).save(any());
    }

    @Test
    void createTeamWithPmAlreadyManagingAnotherTeamThrowsException(){

        TeamsRequestDto requestDto = new TeamsRequestDto();
        requestDto.setPmId(1L);
        requestDto.setTeamName("Java Team");

        Roles pmRole = new Roles();
        pmRole.setRoleId(1L);
        pmRole.setRoleName("PM");

        Teams existingTeam = new Teams();
        existingTeam.setTeamId(2L);
        existingTeam.setTeamName("Python Team");

        Employees pm = new Employees();
        pm.setEmployeeId(1L);
        pm.setName("Shubham");
        pm.setRole(pmRole);
        pm.setManagedTeam(existingTeam);

        when(employeesRepo.findById(1L)).thenReturn(Optional.of(pm));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            teamsService.createTeam(requestDto);
        });

        assertEquals("The PM 'Shubham' is already managing another team.", ex.getMessage());
        verify(employeesRepo, times(1)).findById(1L);
    }

    @Test
    void assignPmSuccess(){
        TeamsRequestDto requestDto = new TeamsRequestDto();
        requestDto.setPmId(1L);

        Roles pmRole = new Roles();
        pmRole.setRoleId(1L);
        pmRole.setRoleName("PM");

        Teams existingTeam = new Teams();
        existingTeam.setTeamId(1L);
        existingTeam.setTeamName("Java Team");

        Employees pm = new Employees();
        pm.setEmployeeId(1L);
        pm.setName("Shubham");
        pm.setRole(pmRole);
        pm.setManagedTeam(null);

        Teams savedTeam = new Teams();
        savedTeam.setTeamId(1L);
        savedTeam.setTeamName("Java Team");
        savedTeam.setPm(pm);

        when(teamsRepo.findById(1L)).thenReturn(Optional.of(existingTeam));
        when(employeesRepo.findById(1L)).thenReturn(Optional.of(pm));
        when(teamsRepo.save(any(Teams.class))).thenReturn(savedTeam);

        TeamsResponseDto response = teamsService.assignPm(1L, requestDto);

        assertNotNull(response);
        assertEquals(1L, response.getTeamId());
        assertEquals("Java Team", response.getTeamName());
        assertEquals(1L, response.getPmId());

        verify(teamsRepo, times(1)).findById(1L);
        verify(employeesRepo, times(1)).findById(1L);
        verify(teamsRepo, times(2)).save(any(Teams.class)); // called twice (before and after updating pm)
        verify(employeesRepo, times(1)).save(any(Employees.class));
    }

    @Test
    void assignPmWithInvalidRoleThrowsException(){
        TeamsRequestDto requestDto = new TeamsRequestDto();
        requestDto.setPmId(3L);

        Roles ttlRole = new Roles();
        ttlRole.setRoleId(2L);
        ttlRole.setRoleName("TTL");

        Teams existingTeam = new Teams();
        existingTeam.setTeamId(1L);
        existingTeam.setTeamName("Java Team");

        Employees ttl = new Employees();
        ttl.setEmployeeId(3L);
        ttl.setName("Shubham");
        ttl.setRole(ttlRole);
        ttl.setManagedTeam(null);

        when(teamsRepo.findById(1L)).thenReturn(Optional.of(existingTeam));
        when(employeesRepo.findById(3L)).thenReturn(Optional.of(ttl));

        APIException ex = assertThrows(APIException.class, () -> {
            teamsService.assignPm(1L, requestDto);
        });

        assertEquals("The employee should have role id 1 for being PM", ex.getMessage());
        verify(teamsRepo, times(1)).findById(1L);
        verify(employeesRepo, times(1)).findById(3L);
        verify(teamsRepo, never()).save(any());
    }

    @Test
    void getTeamByIdSuccess(){

        Teams savedTeam = new Teams();
        savedTeam.setTeamId(1L);
        savedTeam.setTeamName("JAVA");

        when(teamsRepo.findById(1L)).thenReturn(Optional.of(savedTeam));

        TeamsResponseDto response = teamsService.getTeamById(1L);
        assertEquals("JAVA", response.getTeamName());
    }

    @Test
    void teamIdNotFoundThrowsException() {

        Teams savedTeam = new Teams();
        savedTeam.setTeamId(1L);

        when(teamsRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            teamsService.getTeamById(1L);
        });
    }

    @Test
    void updateTeamSuccess() {

        TeamsRequestDto requestDto = new TeamsRequestDto();
        requestDto.setTeamName("Java Team");
        requestDto.setPmId(5L);

        Roles pmRole = new Roles();
        pmRole.setRoleId(1L);
        pmRole.setRoleName("PM");

        Employees oldPm = new Employees();
        oldPm.setEmployeeId(1L);
        oldPm.setName("Shubham1");
        oldPm.setRole(pmRole);
        oldPm.setManagedTeam(null);

        Employees newPm = new Employees();
        newPm.setEmployeeId(5L);
        newPm.setName("Shubham5");
        newPm.setRole(pmRole);
        newPm.setManagedTeam(null);

        Teams existingTeam = new Teams();
        existingTeam.setTeamId(1L);
        existingTeam.setTeamName("JAVA");
        existingTeam.setPm(oldPm);

        Teams updatedTeam = new Teams();
        updatedTeam.setTeamId(1L);
        updatedTeam.setTeamName("Java Team");
        updatedTeam.setPm(newPm);

        when(teamsRepo.findById(1L)).thenReturn(Optional.of(existingTeam));
        when(employeesRepo.findById(5L)).thenReturn(Optional.of(newPm));
        when(teamsRepo.save(any(Teams.class))).thenReturn(updatedTeam);

        TeamsResponseDto response = teamsService.updateTeam(1L, requestDto);

        assertNotNull(response);
        assertEquals(1L, response.getTeamId());
        assertEquals("Java Team", response.getTeamName());
        assertEquals(5L, response.getPmId());

        verify(teamsRepo, times(1)).findById(1L);
        verify(employeesRepo, times(1)).findById(5L);
        verify(teamsRepo, times(1)).save(any(Teams.class));
    }

    @Test
    void updateTeamNotFoundThrowsException() {
        TeamsRequestDto requestDto = new TeamsRequestDto();
        requestDto.setTeamName("Java Team");

        when(teamsRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            teamsService.updateTeam(1L, requestDto);
        });

        verify(teamsRepo, never()).save(any());
    }
}