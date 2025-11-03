package com.example.RatingsApp.ServiceTest;

import com.example.RatingsApp.dto.RolesDto.RolesRequestDto;
import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.service.Roles.RolesServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        requestDto.setRoleName("PM");

        when(rolesRepo.findByRoleName("PM")).thenReturn(Optional.empty());

        Roles savedRole = new Roles();
        savedRole.setRoleId(1L);
        savedRole.setRoleName("PM");
        when(rolesRepo.save(any(Roles.class))).thenReturn(savedRole);

        RolesResponseDto response = rolesService.createRole(requestDto);

        assertNotNull(response);
        assertEquals(1L,response.getRoleId());
        assertEquals("PM", response.getRoleName());
    }

    @Test
    void createRoleAlreadyExistsThrowsException() {

        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleName("PM");

        Roles existing = new Roles();
        existing.setRoleName("PM");

        when(rolesRepo.findByRoleName("PM")).thenReturn(Optional.of(existing));

        APIException ex = assertThrows(APIException.class, () -> {
            rolesService.createRole(requestDto);
        });

        assertEquals("The given role name 'PM' already exists!", ex.getMessage());
        verify(rolesRepo, times(1)).findByRoleName("PM");
        verify(rolesRepo, never()).save(any(Roles.class)); // Should never save since it already exists
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
    void getRoleByIdSuccess(){

        Roles savedRole = new Roles();
        savedRole.setRoleId(1L);
        savedRole.setRoleName("PM");

        when(rolesRepo.findById(1L)).thenReturn(Optional.of(savedRole));

        RolesResponseDto response = rolesService.getRoleById(1L);
        assertEquals("PM", response.getRoleName());
    }

    @Test
    void roleIdNotFoundThrowsException() {

        Roles savedRole = new Roles();
        savedRole.setRoleId(1L);

        when(rolesRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            rolesService.getRoleById(1L);
        });
    }

    @Test
    void updateRole_Success() {

        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleName("Project Manager");

        Roles existingRole = new Roles();
        existingRole.setRoleId(1L);
        existingRole.setRoleName("PM");

        Roles updatedRole = new Roles();
        updatedRole.setRoleId(1L);
        updatedRole.setRoleName("Project Manager");

        when(rolesRepo.findById(1L)).thenReturn(Optional.of(existingRole));
        when(rolesRepo.save(any(Roles.class))).thenReturn(updatedRole);

        RolesResponseDto response = rolesService.updateRole(1L, requestDto);

        assertNotNull(response);
        assertEquals("Project Manager", response.getRoleName());
        assertEquals(1L, response.getRoleId());

        verify(rolesRepo, times(1)).findById(1L);
        verify(rolesRepo, times(1)).save(any(Roles.class));
    }

    @Test
    void updateRoleNotFoundThrowsException() {
        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleName("PM");

        when(rolesRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            rolesService.updateRole(1L, requestDto);
        });

        verify(rolesRepo, never()).save(any());
    }

    @Test
    void deleteRoleSuccess(){

        Roles existingRole = new Roles();
        existingRole.setRoleId(1L);

        when(rolesRepo.findById(1L)).thenReturn(Optional.of(existingRole));
        rolesService.deleteRole(1L);
        verify(rolesRepo, times(1)).deleteById(1L);
        System.out.println("Role Delete Test Success");
    }

    @Test
    void roleIdNotFoundForDeletingThrowsException(){

        RolesRequestDto requestDto = new RolesRequestDto();
        requestDto.setRoleName("PM");

        when(rolesRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            rolesService.updateRole(1L, requestDto);
        });

        verify(rolesRepo, never()).save(any());
    }
}