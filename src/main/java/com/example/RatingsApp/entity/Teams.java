package com.example.RatingsApp.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "team")
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String teamId;

    @Column(nullable = false, unique = true)
    private String teamName;

    @ManyToOne
    @JoinColumn(name = "pm_id", referencedColumnName = "employeeId")
    private Employees pm;

    @OneToMany(mappedBy = "team")
    private List<Employees> members;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Employees getPm() {
        return pm;
    }

    public void setPm(Employees pm) {
        this.pm = pm;
    }

    public List<Employees> getMembers() {
        return members;
    }

    public void setMembers(List<Employees> members) {
        this.members = members;
    }
}
