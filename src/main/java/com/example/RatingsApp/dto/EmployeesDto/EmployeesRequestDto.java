package com.example.RatingsApp.dto.EmployeesDto;

public class EmployeesRequestDto {

    private String name;

    private String email;

    private String password;

    private Long roleId;

    private Long teamId;

    public EmployeesRequestDto(String name, String email, String password, Long roleId, Long teamId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
