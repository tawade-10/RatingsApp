package com.example.RatingsApp.dto.TeamsDto;

import com.example.RatingsApp.entity.Teams;

import java.util.UUID;

public class TeamsResponseDto {

    private Long teamId;

    private String teamName;

    private Long pmId;

    public TeamsResponseDto(Teams teams) {
        this.teamId = teams.getTeamId();
        this.teamName = teams.getTeamName();
        this.pmId = (teams.getPm() != null) ? teams.getPm().getEmployeeId() : null;
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
