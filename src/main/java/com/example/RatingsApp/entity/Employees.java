package com.example.RatingsApp.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @ManyToOne
    @JoinColumn(name = "team_id",  nullable = true)
    private Teams team;

    @OneToMany(mappedBy = "ratedBy")
    private List<Ratings> ratingsGiven;

    @OneToMany(mappedBy = "employee")
    private List<Ratings> ratingsReceived;

    @OneToOne(mappedBy = "pm")
    private Teams managedTeam;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public Teams getTeam() {
        return team;
    }

    public void setTeam(Teams team) {
        this.team = team;
    }

    public List<Ratings> getRatingsGiven() {
        return ratingsGiven;
    }

    public void setRatingsGiven(List<Ratings> ratingsGiven) {
        this.ratingsGiven = ratingsGiven;
    }

    public List<Ratings> getRatingsReceived() {
        return ratingsReceived;
    }

    public void setRatingsReceived(List<Ratings> ratingsReceived) {
        this.ratingsReceived = ratingsReceived;
    }

    public Teams getManagedTeam() {
        return managedTeam;
    }

    public void setManagedTeam(Teams managedTeam) {
        this.managedTeam = managedTeam;
    }
}


