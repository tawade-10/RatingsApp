package com.example.RatingsApp.service.Roles;

import com.example.RatingsApp.dto.RolesDto.RolesRequestDto;
import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.RolesRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RolesServiceImpl implements RolesService {

    private final RolesRepo rolesRepo;

    public RolesServiceImpl(RolesRepo rolesRepo) {
        this.rolesRepo = rolesRepo;
    }

    @Override
    public RolesResponseDto createRole(RolesRequestDto rolesRequestDto) {

        if (rolesRequestDto.getRoleName() == null || Objects.equals(rolesRequestDto.getRoleName(), "")) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }

        Optional<Roles> existingRole = rolesRepo.findByRoleNameIgnoreCase(rolesRequestDto.getRoleName());

        if (existingRole.isPresent()) {
            return new RolesResponseDto(existingRole.get());
        }

        Roles role = new Roles();
        role.setRoleName(rolesRequestDto.getRoleName());
        Roles savedRole = rolesRepo.save(role);
        return new RolesResponseDto(savedRole);
    }

    @Override
    public RolesResponseDto getRoleById(String roleId){

        if(roleId == null){
            throw new IllegalArgumentException("Role id must not be null");
        }

        Roles role = rolesRepo.findByRoleId(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id " + roleId + " not found"));

        return new RolesResponseDto(role);
    }

    @Override
    public List<RolesResponseDto> getAllRoles() {
        List<Roles> roles = rolesRepo.findAll(Sort.by(Sort.Direction.ASC, "roleId"));
        return roles.stream().map(RolesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public RolesResponseDto updateRole(String roleId, RolesRequestDto rolesRequestDto) {
        Roles existingRole = rolesRepo.findByRoleId(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role with the given id doesn't exist: " + roleId));

        existingRole.setRoleName(rolesRequestDto.getRoleName());
        Roles updatedRole = rolesRepo.save(existingRole);
        return new RolesResponseDto(updatedRole);
    }

    @Override
    public RolesResponseDto deleteRole(String roleId) {
        Roles role = rolesRepo.findByRoleId(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role with the given id doesn't exist : "+ roleId)
        );
        rolesRepo.delete(role);
        return null;
    }
}
