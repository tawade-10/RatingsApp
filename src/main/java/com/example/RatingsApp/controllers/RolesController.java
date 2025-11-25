package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.RolesDto.RolesRequestDto;
import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.service.Roles.RolesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private final RolesService rolesService;

    @Autowired
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @PostMapping
    public ResponseEntity<RolesResponseDto> createRole(@Valid @RequestBody RolesRequestDto rolesRequestDto) {
        RolesResponseDto savedRole = rolesService.createRole(rolesRequestDto);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    @GetMapping("{roleId}")
    public ResponseEntity<RolesResponseDto> getRoleById(@PathVariable("roleId") Long roleId){
        RolesResponseDto rolesResponseDto = rolesService.getRoleById(roleId);
        return ResponseEntity.ok(rolesResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<RolesResponseDto>> getAllRoles(){
        List<RolesResponseDto> listRoles = rolesService.getAllRoles();
        return ResponseEntity.ok(listRoles);
    }

    @PutMapping("{roleId}")
    public ResponseEntity<RolesResponseDto> updateRole(@PathVariable("roleId") Long roleId, @RequestBody RolesRequestDto updatedRole){
        RolesResponseDto rolesResponseDto = rolesService.updateRole(roleId,updatedRole);
        return ResponseEntity.ok(rolesResponseDto);
    }

    @DeleteMapping("{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable("roleId") Long roleId){
        RolesResponseDto rolesResponseDto = rolesService.deleteRole(roleId);
        return ResponseEntity.ok("Role deleted successfully!");
    }
}
