package com.example.RatingsApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name="roles")
public class Roles {

//    @Id
//    @GenericGenerator(name = "custom-id", type = com.example.RatingsApp.config.CustomIdGenerator.class)
//    @GeneratedValue(generator = "custom-id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false, unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<Employees> employees;

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

    public List<Employees> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employees> employees) {
        this.employees = employees;
    }
}
