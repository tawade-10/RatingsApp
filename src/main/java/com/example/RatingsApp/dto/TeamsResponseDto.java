package com.example.RatingsApp.dto;

import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Teams;

public class TeamsResponseDto {

    private Long teamId;

    private String teamName;

    private Long pmId;

    public TeamsResponseDto(Teams teams) {
        this.teamId = teams.getTeamId();
        this.teamName = teams.getTeamName();
        this.pmId = teams.getPm().getEmployeeId();
    }

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

    public Long getPmId() {
        return pmId;
    }

    public void setPm(Long pmId) {
        this.pmId = pmId;
    }
}
