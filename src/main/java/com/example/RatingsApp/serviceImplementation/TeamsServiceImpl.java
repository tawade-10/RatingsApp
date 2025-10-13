package com.example.RatingsApp.serviceImplementation;

import com.example.RatingsApp.dto.TeamsRequestDto;
import com.example.RatingsApp.dto.TeamsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.TeamsRepo;
import com.example.RatingsApp.service.TeamsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamsServiceImpl implements TeamsService {

    private final TeamsRepo teamsRepo;

    private final EmployeesRepo employeesRepo;

    public TeamsServiceImpl(TeamsRepo teamsRepo, EmployeesRepo employeesRepo) {
        this.teamsRepo = teamsRepo;
        this.employeesRepo = employeesRepo;
    }

    @Override
    public TeamsResponseDto createTeam(TeamsRequestDto teamsRequestDto) {
        Teams team = new Teams();
        team.setTeamName(teamsRequestDto.getTeamName());
        Teams savedTeam = teamsRepo.save(team);
        return new TeamsResponseDto(savedTeam);
    }

    @Override
    public TeamsResponseDto assignPm(Long teamId , TeamsRequestDto teamsRequestDto) {
        Teams team = teamsRepo.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with the given id doesn't exist: " + teamId)
        );
        Long pmId = teamsRequestDto.getPmId();
        if (pmId == null) {
            throw new IllegalArgumentException("PM ID must be provided in the request body.");
        }
        Employees pm = employeesRepo.findById(pmId).orElseThrow(
                () -> new ResourceNotFoundException("Employee (PM) not found with ID: " + pmId)
        );
        team.setPm(pm);
        pm.setManagedTeam(team);
        Teams updatedTeamsObj = teamsRepo.save(team);
        return new TeamsResponseDto(updatedTeamsObj);
    }

    @Override
    public TeamsResponseDto getTeamById(Long teamId) {
        Teams team = teamsRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        return new TeamsResponseDto(team);
    }

    @Override
    public TeamsResponseDto getTeamByName(String teamName) {
        Teams team = teamsRepo.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamName));
        return new TeamsResponseDto(team);
    }
}
