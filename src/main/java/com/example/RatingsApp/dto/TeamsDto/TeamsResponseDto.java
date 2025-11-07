package com.example.RatingsApp.dto.TeamsDto;

import com.example.RatingsApp.entity.Teams;

import java.util.UUID;

public class TeamsResponseDto {

    private String teamId;

    private String teamName;

    private String pmId;

    public TeamsResponseDto(Teams teams) {
        this.teamId = teams.getTeamId();
        this.teamName = teams.getTeamName();
        this.pmId = (teams.getPm() != null) ? teams.getPm().getEmployeeId() : null;
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

    public String getPmId() {
        return pmId;
    }

    public void setPm(String pmId) {
        this.pmId = pmId;
    }
}
