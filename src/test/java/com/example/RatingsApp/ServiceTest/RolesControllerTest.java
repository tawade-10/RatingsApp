package com.example.RatingsApp.ServiceTest;

import com.example.RatingsApp.dto.RolesDto.RolesRequestDto;
import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.service.Roles.RolesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolesServiceImplTest {

    @Mock
    private RolesRepo rolesRepo;

    @InjectMocks
    private RolesServiceImpl rolesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRoleSuccess() {

        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleId("R101");
        requestDto.setRoleName("PM");

        when(rolesRepo.findByRoleIdIgnoreCase("R101")).thenReturn(Optional.empty());
        when(rolesRepo.findByRoleNameIgnoreCase("PM")).thenReturn(Optional.empty());

        Roles savedRole = new Roles();
        savedRole.setRoleId("R101");
        savedRole.setRoleName("PM");
        when(rolesRepo.save(any(Roles.class))).thenReturn(savedRole);

        RolesResponseDto response = rolesService.createRole(requestDto);

        assertNotNull(response);
        assertEquals("R101",response.getRoleId());
        assertEquals("PM", response.getRoleName());
    }

    @Test
    void createRoleAlreadyExistsThrowsException() {

        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleId("R101");
        requestDto.setRoleName("PM");

        Roles existing = new Roles();
        existing.setRoleId("R101");
        existing.setRoleName("PM");

        when(rolesRepo.findByRoleIdIgnoreCase("R101")).thenReturn(Optional.of(existing));

        APIException ex = assertThrows(APIException.class, () -> {
            rolesService.createRole(requestDto);
        });

        assertEquals("The given role ID 'R101' already exists!", ex.getMessage());
        verify(rolesRepo, times(1)).findByRoleIdIgnoreCase("R101");
        verify(rolesRepo, never()).save(any(Roles.class));
    }

    @Test
    void createRoleNotNullThrowsException(){
        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleName(null);

        assertThrows(IllegalArgumentException.class, () -> {
            rolesService.createRole(requestDto);
        });
    }

    @Test
    void getAllRolesSuccess() {

        Roles role1 = new Roles();
        role1.setRoleId("R101");
        role1.setRoleName("PM");

        Roles role2 = new Roles();
        role2.setRoleId("R102");
        role2.setRoleName("TL");

        when(rolesRepo.findAll(Sort.by(Sort.Direction.ASC, "roleId")))
                .thenReturn(List.of(role1, role2));

        List<RolesResponseDto> rolesList = rolesService.getAllRoles();

        assertNotNull(rolesList);
        assertEquals(2, rolesList.size());
        assertEquals("PM", rolesList.get(0).getRoleName());
        assertEquals("TL", rolesList.get(1).getRoleName());
        verify(rolesRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "roleId"));
    }

    @Test
    void getAllRolesEmptyList(){

        when(rolesRepo.findAll()).thenReturn(List.of());

        List<RolesResponseDto> rolesList = rolesService.getAllRoles();

        assertNotNull(rolesList);
        assertTrue(rolesList.isEmpty());
        verify(rolesRepo, times(1)).findAll(Sort.by(Sort.Direction.ASC, "roleId"));
    }

    @Test
    void getRoleByIdSuccess(){

        Roles savedRole = new Roles();
        savedRole.setRoleId("R101");
        savedRole.setRoleName("PM");

        when(rolesRepo.findByRoleIdIgnoreCase("R101")).thenReturn(Optional.of(savedRole));

        RolesResponseDto response = rolesService.getRoleById("R101");
        assertEquals("PM", response.getRoleName());
    }

    @Test
    void roleIdNotFoundThrowsException() {

        Roles savedRole = new Roles();
        savedRole.setRoleId("R101");

        when(rolesRepo.findByRoleIdIgnoreCase("R101")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            rolesService.getRoleById("R101");
        });
    }

    @Test
    void updateRoleSuccess() {

        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleId("R101");
        requestDto.setRoleName("PM");

        Roles existingRole = new Roles();
        existingRole.setRoleId("R101");
        existingRole.setRoleName("PM");

        Roles updatedRole = new Roles();
        updatedRole.setRoleId("R101");
        updatedRole.setRoleName("Project Manager");

        when(rolesRepo.findByRoleIdIgnoreCase("R101")).thenReturn(Optional.of(existingRole));
        when(rolesRepo.save(any(Roles.class))).thenReturn(updatedRole);

        RolesResponseDto response = rolesService.updateRole("R101", requestDto);

        assertNotNull(response);
        assertEquals("R101", response.getRoleId());
        assertEquals("Project Manager", response.getRoleName());

        verify(rolesRepo, times(1)).findByRoleIdIgnoreCase("R101");
        verify(rolesRepo, times(1)).save(any(Roles.class));
    }

    @Test
    void updateRoleNotFoundThrowsException() {
        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleId("R101");
        requestDto.setRoleName("PM");

        when(rolesRepo.findByRoleIdIgnoreCase("R101")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            rolesService.updateRole("R101", requestDto);
        });

        verify(rolesRepo, never()).save(any());
    }

    @Test
    void deleteRoleSuccess(){

        Roles existingRole = new Roles();
        existingRole.setRoleId("R101");

        when(rolesRepo.findByRoleIdIgnoreCase("R101")).thenReturn(Optional.of(existingRole));
        rolesService.deleteRole("R101");
        verify(rolesRepo, times(1)).findByRoleIdIgnoreCase("R101");
        System.out.println("Role Delete Test Success");
    }

    @Test
    void roleIdNotFoundForDeletingThrowsException(){

        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleId("R101");
        requestDto.setRoleName("PM");

        when(rolesRepo.findByRoleIdIgnoreCase("R01")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            rolesService.updateRole("R101", requestDto);
        });

        verify(rolesRepo, never()).save(any());
    }
}