package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.TeamsDto.TeamsRequestDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;
import com.example.RatingsApp.service.Teams.TeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamsController {

    private final TeamsService teamsService;

    @Autowired
    public TeamsController(TeamsService teamsService) {
        this.teamsService = teamsService;
    }

    @PostMapping
    public ResponseEntity<TeamsResponseDto> createTeam(@RequestBody TeamsRequestDto teamsRequestDto){
        TeamsResponseDto savedTeam = teamsService.createTeam(teamsRequestDto);
        return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamsResponseDto> assignPmId(
            @PathVariable Long teamId,
            @RequestBody TeamsRequestDto teamsRequestDto) {
        TeamsResponseDto savedPmId = teamsService.assignPm(teamId, teamsRequestDto);
        return new ResponseEntity<>(savedPmId, HttpStatus.OK);
    }

    @GetMapping("/id/{teamId}")
    public ResponseEntity<TeamsResponseDto> getTeamById(@PathVariable Long teamId) {
        TeamsResponseDto teamResponse = teamsService.getTeamById(teamId);
        return ResponseEntity.ok(teamResponse);
    }

    @GetMapping("/name/{teamName}")
    public ResponseEntity<TeamsResponseDto> getTeamByName(@PathVariable String teamName){
        TeamsResponseDto teamResponse = teamsService.getTeamByName(teamName);
        return ResponseEntity.ok(teamResponse);
    }

    @GetMapping
    public ResponseEntity<List<TeamsResponseDto>> getAllTeams(){
        List<TeamsResponseDto> listTeams = teamsService.getAllTeams();
        return ResponseEntity.ok(listTeams);
    }

    @PutMapping("/update_team/{teamId}")
    public ResponseEntity<TeamsResponseDto> updateTeam(
            @PathVariable Long teamId,
            @RequestBody TeamsRequestDto teamsRequestDto) {
        TeamsResponseDto savedUpdatedTeam = teamsService.updateTeam(teamId, teamsRequestDto);
        return new ResponseEntity<>(savedUpdatedTeam, HttpStatus.OK);
    }

    @DeleteMapping("{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId){
        TeamsResponseDto deletedTeam = teamsService.deleteTeam(teamId);
        return ResponseEntity.ok("Team Deleted!");
    }
}
