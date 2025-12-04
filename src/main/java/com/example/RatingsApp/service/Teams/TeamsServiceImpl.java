package com.example.RatingsApp.service.Teams;

import com.example.RatingsApp.dto.RolesDto.RolesResponseDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsRequestDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Roles;
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
    public TeamsResponseDto createTeam(TeamsRequestDto teamsRequestDto, Long roleId) {

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
    public TeamsResponseDto assignPm(Long teamId , TeamsRequestDto teamsRequestDto) {
        Teams team = teamsRepo.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with the given id doesn't exist: " + teamId)
        );
        Long pmId = teamsRequestDto.getPmId();
        if (pmId == null) {
            throw new IllegalArgumentException("PM ID must be provided.");
        }
        Employees pm = employeesRepo.findById(pmId).orElseThrow(
                () -> new ResourceNotFoundException("Employee (PM) not found with ID: " + pmId)
        );

        if(!(pm.getRole().getRoleId() == 2L)){
            throw new APIException("The employee role id doesn't match for being PM");
        }

        team.setPm(pm);
        teamsRepo.save(team);

        pm.setTeam(team);
        employeesRepo.save(pm);

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
    public TeamsResponseDto updateTeam(Long teamId, TeamsRequestDto teamsRequestDto) {

        Teams team = teamsRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

//        String pmId = teamsRequestDto.getPmId();
//
//        if (pmId == null) {
//            throw new IllegalArgumentException("PM ID must be provided in the request body.");
//        }
//        Employees pm = employeesRepo.findByEmployeeIdIgnoreCase(pmId).orElseThrow(
//                () -> new ResourceNotFoundException("Employee (PM) not found with ID: " + pmId)
//        );
//
//        if(!Objects.equals(pm.getRole().getRoleId(), "R101")){
//            throw new APIException("The employee should have role id R101 for being PM");
//        }

        team.setTeamName(teamsRequestDto.getTeamName());
//        team.setPm(pm);
        Teams updatedTeam = teamsRepo.save(team);
        return new TeamsResponseDto(updatedTeam);
    }

    @Override
    public TeamsResponseDto deleteTeam(Long teamId) {
        Teams team = teamsRepo.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));
        teamsRepo.delete(team);
        return null;
    }
}
