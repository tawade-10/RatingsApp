package com.example.RatingsApp.service;

import com.example.RatingsApp.dto.RolesRequestDto;
import com.example.RatingsApp.dto.RolesResponseDto;
import com.example.RatingsApp.entity.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    RolesResponseDto createRole(RolesRequestDto roleRequestDto);

    Optional<Roles> getRoleById(Long roleId);

    List<RolesResponseDto> getAllRoles();

    RolesResponseDto updateRole(Long roleId, RolesRequestDto updatedRole);

    RolesResponseDto deleteRole(Long roleId);
}
