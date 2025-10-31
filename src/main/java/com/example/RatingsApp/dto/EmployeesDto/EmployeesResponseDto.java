package com.example.RatingsApp.dto.EmployeesDto;

import com.example.RatingsApp.entity.Employees;

public class EmployeesResponseDto {

    private Long employeeId;

    private String name;

    private String email;

    private Long roleId;

    private String roleName;

    private Long teamId;

    private String teamName;

    public EmployeesResponseDto(Employees employees) {
        this.employeeId = employees.getEmployeeId();
        this.name = employees.getName();
        this.email = employees.getEmail();
        this.roleId = employees.getRole().getRoleId();
        this.roleName = employees.getRole().getRoleName();
        this.teamId = (employees.getTeam() != null) ? employees.getTeam().getTeamId() : null;
        this.teamName = (employees.getTeam() != null) ? employees.getTeam().getTeamName() : null;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
