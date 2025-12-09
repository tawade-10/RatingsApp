//package com.example.RatingsApp.ServiceTest;
//
//import com.example.RatingsApp.dto.RolesDto.RolesRequestDto;
//import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
//import com.example.RatingsApp.entity.Roles;
//import com.example.RatingsApp.exception.APIException;
//import com.example.RatingsApp.exception.ResourceNotFoundException;
//import com.example.RatingsApp.repository.RolesRepo;
//import com.example.RatingsApp.service.Roles.RolesServiceImpl;
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
//import static org.mockito.Mockito.*;
//
//class RolesServiceImplTest {
//
//    @Mock
//    private RolesRepo rolesRepo;
//
//    @InjectMocks
//    private RolesServiceImpl rolesService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createRoleSuccess() {
//
//        RolesRequestDto requestDto = new RolesRequestDto();
//        requestDto.setRoleName("PM");
//
//        when(rolesRepo.findByRoleNameIgnoreCase("PM")).thenReturn(Optional.empty());
//
//        Roles savedRole = new Roles();
//        savedRole.setRoleName("PM");
//
//        when(rolesRepo.save(any(Roles.class))).thenReturn(savedRole);
//
//        RolesResponseDto response = rolesService.createRole(requestDto);
//
//        assertNotNull(response);
//        assertEquals("PM", response.getRoleName());
//        verify(rolesRepo, times(1)).save(any(Roles.class));
//    }
//
//    @Test
//    void createRoleAlreadyExists_ThrowsException() {
//
//        RolesRequestDto requestDto = new RolesRequestDto();
//        requestDto.setRoleName("PM");
//
//        Roles existing = new Roles();
//        existing.setRoleId("ROL002");
//        existing.setRoleName("PM");
//
//        when(rolesRepo.findByRoleNameIgnoreCase("PM")).thenReturn(Optional.of(existing));
//
//        APIException ex = assertThrows(APIException.class, () -> {
//            rolesService.createRole(requestDto);
//        });
//
//        assertEquals("The given role 'PM' already exists!", ex.getMessage());
//        verify(rolesRepo, never()).save(any(Roles.class));
//    }
//
//    @Test
//    void createRole_NullRoleName_ThrowsException(){
//
//        RolesRequestDto requestDto = new RolesRequestDto();
//        requestDto.setRoleName(null);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            rolesService.createRole(requestDto);
//        });
//    }
//
//    @Test
//    void getAllRolesSuccess() {
//
//        Roles role1 = new Roles();
//        role1.setRoleId("ROL001");
//        role1.setRoleName("PM");
//
//        Roles role2 = new Roles();
//        role2.setRoleId("ROL002");
//        role2.setRoleName("TL");
//
//        when(rolesRepo.findAll(Sort.by(Sort.Direction.ASC, "roleId")))
//                .thenReturn(List.of(role1, role2));
//
//        List<RolesResponseDto> list = rolesService.getAllRoles();
//
//        assertEquals(2, list.size());
//        assertEquals("PM", list.get(0).getRoleName());
//        assertEquals("TL", list.get(1).getRoleName());
//    }
//
//    @Test
//    void getAllRoles_EmptyList_ReturnsEmpty() {
//
//        when(rolesRepo.findAll(Sort.by(Sort.Direction.ASC, "roleId"))).thenReturn(List.of());
//
//        List<RolesResponseDto> list = rolesService.getAllRoles();
//
//        assertNotNull(list);
//        assertTrue(list.isEmpty());
//    }
//
//    @Test
//    void getRoleByIdSuccess() {
//
//        Roles role = new Roles();
//        role.setRoleId("ROL002");
//        role.setRoleName("PM");
//
//        when(rolesRepo.findByRoleId("ROL002")).thenReturn(Optional.of(role));
//
//        RolesResponseDto response = rolesService.getRoleById("ROL002");
//
//        assertNotNull(response);
//        assertEquals("PM", response.getRoleName());
//    }
//
//    @Test
//    void getRoleById_NotFound_ThrowsException() {
//
//        when(rolesRepo.findByRoleId("ROL002")).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            rolesService.getRoleById("ROL002");
//        });
//    }
//
//    @Test
//    void updateRoleSuccess() {
//
//        RolesRequestDto requestDto = new RolesRequestDto();
//        requestDto.setRoleName("Project Manager");
//
//        Roles existing = new Roles();
//        existing.setRoleId("ROL002");
//        existing.setRoleName("PM");
//
//        when(rolesRepo.findByRoleId("ROL002")).thenReturn(Optional.of(existing));
//
//        Roles updated = new Roles();
//        updated.setRoleId("ROL002");
//        updated.setRoleName("Project Manager");
//
//        when(rolesRepo.save(any(Roles.class)))
//                .thenReturn(updated);
//
//        RolesResponseDto response = rolesService.updateRole("ROL002", requestDto);
//
//        assertNotNull(response);
//        assertEquals("Project Manager", response.getRoleName());
//    }
//
//    @Test
//    void updateRole_NotFound_ThrowsException() {
//
//        RolesRequestDto requestDto = new RolesRequestDto();
//        requestDto.setRoleName("PM");
//
//        when(rolesRepo.findByRoleId("ROL002")).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            rolesService.updateRole("ROL002", requestDto);
//        });
//
//        verify(rolesRepo, never()).save(any());
//    }
//
//    @Test
//    void deleteRoleSuccess() {
//
//        Roles role = new Roles();
//        role.setRoleId("ROL002");
//        role.setRoleName("PM");
//
//        when(rolesRepo.findByRoleId("ROL002")).thenReturn(Optional.of(role));
//
//        rolesService.deleteRole("ROL002");
//
//        verify(rolesRepo, times(1)).findByRoleId("ROL002");
//        verify(rolesRepo, times(1)).delete(role);
//    }
//
//    @Test
//    void deleteRole_NotFound_ThrowsException() {
//
//        when(rolesRepo.findByRoleId("ROL002")).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            rolesService.deleteRole("ROL002");
//        });
//
//        verify(rolesRepo, never()).delete(any());
//    }
//}
