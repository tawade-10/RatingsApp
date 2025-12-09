package com.example.RatingsApp.service.Teams;

import com.example.RatingsApp.dto.TeamsDto.TeamsRequestDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.TeamsRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamsServiceImpl implements TeamsService {

    private final TeamsRepo teamsRepo;

    private final EmployeesRepo employeesRepo;

    public TeamsServiceImpl(TeamsRepo teamsRepo, EmployeesRepo employeesRepo) {
        this.teamsRepo = teamsRepo;
        this.employeesRepo = employeesRepo;
    }

    @Override
    public TeamsResponseDto createTeam(TeamsRequestDto teamsRequestDto, String roleId) {

        Optional<Teams> existingTeam = teamsRepo.findByTeamNameIgnoreCase(teamsRequestDto.getTeamName());
        if (existingTeam.isPresent()) {
            return new TeamsResponseDto(existingTeam.get());
        }

        Teams team = new Teams();
//      team.setTeamId(teamsRequestDto.getTeamId());
        team.setTeamName(teamsRequestDto.getTeamName());
        //  team.setPm(pm);

        Teams savedTeam = teamsRepo.save(team);

        //  pm.setTeam(savedTeam);
        //  employeesRepo.save(pm);

        return new TeamsResponseDto(savedTeam);
    }

    @Override
    public TeamsResponseDto assignPm(String teamId, TeamsRequestDto teamsRequestDto) {

        Teams team = teamsRepo.findByTeamId(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with the given id doesn't exist: " + teamId)
        );

        String pmId = teamsRequestDto.getPmId();
        if (pmId == null) {
            throw new IllegalArgumentException("PM ID must be provided.");
        }

        Employees pm = employeesRepo.findByEmployeeId(pmId).orElseThrow(
                () -> new ResourceNotFoundException("Employee (PM) not found with ID: " + pmId)
        );

        if (!Objects.equals(pm.getRole().getRoleId(), "ROL002")) {
            throw new APIException("This employee is not eligible to be a PM (Invalid role)");
        }

        if (team.getPm() != null) {
            Employees existingPm = team.getPm();
            if (!existingPm.getEmployeeId().equals(pmId)) {
                return new TeamsResponseDto(team);
            }
            return new TeamsResponseDto(team);
        }

        team.setPm(pm);
        pm.setTeam(team);

        teamsRepo.save(team);
        employeesRepo.save(pm);

        return new TeamsResponseDto(team);
    }

    @Override
    public TeamsResponseDto getTeamById(String teamId) {
        Teams team = teamsRepo.findByTeamId(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        return new TeamsResponseDto(team);
    }

    @Override
    public TeamsResponseDto getTeamByName(String teamName) {
        Teams team = teamsRepo.findByTeamNameIgnoreCase(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with name: " + teamName));
        return new TeamsResponseDto(team);
    }

    @Override
    public List<TeamsResponseDto> getAllTeams() {
        List<Teams> teams = teamsRepo.findAll(Sort.by(Sort.Direction.ASC, "teamId"));
        return teams.stream().map(TeamsResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public TeamsResponseDto updateTeam(String teamId, TeamsRequestDto teamsRequestDto) {

        Teams team = teamsRepo.findByTeamId(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        team.setTeamName(teamsRequestDto.getTeamName());
//        team.setPm(pm);
        Teams updatedTeam = teamsRepo.save(team);
        return new TeamsResponseDto(updatedTeam);
    }

    @Override
    public TeamsResponseDto deleteTeam(String teamId) {
        Teams team = teamsRepo.findByTeamId(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));
        teamsRepo.delete(team);
        return null;
    }
}
