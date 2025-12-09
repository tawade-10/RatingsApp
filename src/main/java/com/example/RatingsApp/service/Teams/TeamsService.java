package com.example.RatingsApp.service.Teams;

import com.example.RatingsApp.dto.TeamsDto.TeamsRequestDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;

import java.util.List;

public interface TeamsService {
    TeamsResponseDto createTeam(TeamsRequestDto teamsRequestDto, String roleId);

    TeamsResponseDto assignPm(String teamId, TeamsRequestDto teamsRequestDto);
    TeamsResponseDto getTeamById(String teamId);

    TeamsResponseDto getTeamByName(String teamName);

    List<TeamsResponseDto> getAllTeams();

    TeamsResponseDto updateTeam(String teamId, TeamsRequestDto teamsRequestDto);

    TeamsResponseDto deleteTeam(String teamId);
}
