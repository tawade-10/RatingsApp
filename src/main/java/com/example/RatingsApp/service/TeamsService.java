package com.example.RatingsApp.service;

import com.example.RatingsApp.dto.TeamsRequestDto;
import com.example.RatingsApp.dto.TeamsResponseDto;
import com.example.RatingsApp.entity.Teams;

import java.util.Optional;

public interface TeamsService {
    TeamsResponseDto createTeam(TeamsRequestDto teamsRequestDto);

    TeamsResponseDto assignPm(Long teamId, TeamsRequestDto teamsRequestDto);
    TeamsResponseDto getTeamById(Long teamId);

    TeamsResponseDto getTeamByName(String teamName);
}
