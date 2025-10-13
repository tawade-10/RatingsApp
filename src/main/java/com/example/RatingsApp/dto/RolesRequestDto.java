package com.example.RatingsApp.dto;

public class RolesRequestDto {

    private String roleName;

    public RolesRequestDto(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
