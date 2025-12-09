package com.example.RatingsApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "team")
public class Teams {

    @Id
    @GeneratedValue(generator = "team-id-generator")
    @GenericGenerator(name = "team-id-generator",
            strategy = "com.example.RatingsApp.config.CustomIdGenerator")
    private String teamId;

    @Column(nullable = false, unique = true)
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pm_id")
    private Employees pm;

    @OneToMany(mappedBy = "team")
    private List<Employees> members;

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
