package com.example.RatingsApp.dto.EmployeesDto;

import com.example.RatingsApp.entity.Employees;

import java.util.UUID;

public class EmployeesResponseDto {

    private String employeeId;

    private String name;

    private String email;

    private String roleId;

    private String roleName;

    private String teamId;

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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
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
