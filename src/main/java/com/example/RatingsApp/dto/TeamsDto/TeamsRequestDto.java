package com.example.RatingsApp.dto.TeamsDto;

public class TeamsRequestDto {

    private String teamId;

    private String teamName;

    private String pmId;

    public TeamsRequestDto() {
    }

    public TeamsRequestDto(String teamId, String teamName, String pmId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.pmId = pmId;
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

    public void setPmId(String pmId) {
        this.pmId = pmId;
    }
}
