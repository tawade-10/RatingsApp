package com.example.RatingsApp.dto.TeamsDto;

public class TeamsRequestDto {

//    private String teamId;

    private String teamName;

    private Long pmId;

    public TeamsRequestDto() {
    }

    public TeamsRequestDto(String teamName, Long pmId) {
//        this.teamId = teamId;
        this.teamName = teamName;
        this.pmId = pmId;
    }

//    public String getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(String teamId) {
//        this.teamId = teamId;
//    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getPmId() {
        return pmId;
    }

    public void setPmId(Long pmId) {
        this.pmId = pmId;
    }
}
