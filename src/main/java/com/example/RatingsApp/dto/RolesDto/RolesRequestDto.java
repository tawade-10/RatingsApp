package com.example.RatingsApp.dto.RolesDto;

public class RolesRequestDto {

    private String roleId;

    private String roleName;

    public RolesRequestDto() {
    }

    public RolesRequestDto(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
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
