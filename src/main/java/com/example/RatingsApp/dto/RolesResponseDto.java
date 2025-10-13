package com.example.RatingsApp.dto;

import com.example.RatingsApp.entity.Roles;

public class RolesResponseDto {

    private Long roleId;

    private String roleName;

    public RolesResponseDto(Roles role) {
        this.roleId = role.getRoleId();
        this.roleName = role.getRoleName();
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
