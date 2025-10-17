package com.example.RatingsApp.service;

import com.example.RatingsApp.dto.TeamsDto.TeamsRequestDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;

import java.util.List;

public interface TeamsService {
    TeamsResponseDto createTeam(TeamsRequestDto teamsRequestDto);

    TeamsResponseDto assignPm(Long teamId, TeamsRequestDto teamsRequestDto);
    TeamsResponseDto getTeamById(Long teamId);

    TeamsResponseDto getTeamByName(String teamName);

    List<TeamsResponseDto> getAllTeams();

    TeamsResponseDto updateTeam(Long teamId, TeamsRequestDto teamsRequestDto);

    TeamsResponseDto deleteTeam(Long teamId);
}
