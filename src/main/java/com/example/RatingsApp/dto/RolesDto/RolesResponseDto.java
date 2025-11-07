package com.example.RatingsApp.dto.RolesDto;

import com.example.RatingsApp.entity.Roles;

public class RolesResponseDto {

    private Long id;

    private String roleId;

    private String roleName;

    public RolesResponseDto(Roles role) {
        this.id = role.getId();
        this.roleId = role.getRoleId();
        this.roleName = role.getRoleName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
