package com.example.RatingsApp.dto;

public class LoginResponse {

    private String token;
    private String employeeId;
    private String email;
    private String role;

    public LoginResponse(String token, String employeeId, String email, String role) {
        this.token = token;
        this.employeeId = employeeId;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
