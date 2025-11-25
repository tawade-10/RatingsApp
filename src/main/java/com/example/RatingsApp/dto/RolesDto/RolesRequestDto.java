package com.example.RatingsApp.dto.RolesDto;

public class RolesRequestDto {

    private String roleName;

    public RolesRequestDto() {
    }

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
