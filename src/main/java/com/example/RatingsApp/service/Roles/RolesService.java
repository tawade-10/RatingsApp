package com.example.RatingsApp.service.Roles;

import com.example.RatingsApp.dto.RolesDto.RolesRequestDto;
import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
import com.example.RatingsApp.entity.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    RolesResponseDto createRole(RolesRequestDto roleRequestDto);

    RolesResponseDto getRoleById(Long roleId);

    List<RolesResponseDto> getAllRoles();

    RolesResponseDto updateRole(Long roleId, RolesRequestDto updatedRole);

    RolesResponseDto deleteRole(Long roleId);
}
