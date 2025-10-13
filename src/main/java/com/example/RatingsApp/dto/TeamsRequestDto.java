package com.example.RatingsApp.dto;

import com.example.RatingsApp.entity.Employees;

public class TeamsRequestDto {

    private String teamName;

    private Long pmId;

    public TeamsRequestDto(String teamName, Long pmId) {
        this.teamName = teamName;
        this.pmId = pmId;
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

    public void setPmId(Long pmId) {
        this.pmId = pmId;
    }
}
