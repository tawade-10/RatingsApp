package com.example.RatingsApp.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

//lombok @Getter @Setter
@Entity
@Table(name = "employee")
public class Employees implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "roleId", nullable = false)
    private Roles role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "teamId", nullable = true)
    private Teams team;

    @OneToMany(mappedBy = "ratedBy") // why
    private List<Ratings> ratingsGiven;

    @OneToMany(mappedBy = "employee")
    private List<Ratings> ratingsReceived;

    @OneToMany(mappedBy = "pm")
    private List<Teams> managedTeams = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleId()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
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

    public List<Teams> getManagedTeams() {
        return managedTeams;
    }

    public void setManagedTeams(List<Teams> managedTeams) {
        this.managedTeams = managedTeams;
    }
}


