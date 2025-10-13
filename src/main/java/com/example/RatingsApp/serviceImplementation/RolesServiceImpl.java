package com.example.RatingsApp.serviceImplementation;

import com.example.RatingsApp.dto.RolesRequestDto;
import com.example.RatingsApp.dto.RolesResponseDto;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.RolesRepo;
import com.example.RatingsApp.service.RolesService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Roles role = new Roles();
        role.setRoleName(rolesRequestDto.getRoleName());
        Roles savedRole = rolesRepo.save(role);
        return new RolesResponseDto(savedRole);
    }

    @Override
    public Optional<Roles> getRoleById(Long roleId){
        if(roleId == null){
            throw new IllegalArgumentException("Role id must not be null");
        }
        return rolesRepo.findById(roleId);
    }

    @Override
    public List<RolesResponseDto> getAllRoles() {
        List<Roles> roles = rolesRepo.findAll(Sort.by(Sort.Direction.ASC, "roleId"));
        return roles.stream().map(RolesResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public RolesResponseDto updateRole(Long roleId, RolesRequestDto updatedRole) {
        Roles role = rolesRepo.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role with the given id doesn't exist : "+ roleId)
        );

        role.setRoleName(updatedRole.getRoleName());
        Roles updatedRoleObj = rolesRepo.save(role);
        return new RolesResponseDto(updatedRoleObj);
    }

    @Override
    public RolesResponseDto deleteRole(Long roleId) {
        Roles role = rolesRepo.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role with the given id doesn't exist : "+ roleId)
        );
        rolesRepo.deleteById(roleId);
        return null;
    }
}
