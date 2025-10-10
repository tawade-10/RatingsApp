package com.example.RatingsApp.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "team")
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false, unique = true)
    private String teamName;

    @OneToOne
    @JoinColumn(name = "pm_id", unique = true)
    private Employees pm;

    @OneToMany(mappedBy = "team")
    private List<Employees> members;

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
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
