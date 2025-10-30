package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.service.Employees.EmployeesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

    private final EmployeesService employeesService;

    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @PostMapping
    public ResponseEntity<EmployeesResponseDto> createEmployee(@RequestBody EmployeesRequestDto employeesRequestDto) {
        EmployeesResponseDto savedEmployee = employeesService.createEmployee(employeesRequestDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("{employeeId}")
    public ResponseEntity<EmployeesResponseDto> getEmployeeById(@PathVariable Long employeeId){
        EmployeesResponseDto getEmployee = employeesService.getEmployeeById(employeeId);
        return ResponseEntity.ok(getEmployee);
    }

    @GetMapping
    public ResponseEntity<List<EmployeesResponseDto>> getAllEmployees(){
        List<EmployeesResponseDto> listEmployees = employeesService.getAllEmployees();
        return ResponseEntity.ok(listEmployees);
    }

    @GetMapping("/employees_by_name")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesByName(@RequestParam String name){
        List<EmployeesResponseDto> listEmployeesByName = employeesService.getEmployeeByName(name);
        return ResponseEntity.ok(listEmployeesByName);
    }

    @GetMapping("/{teamId}/employees_by_team")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesByTeam(@PathVariable Long teamId){
        List<EmployeesResponseDto> listEmployeesByTeam = employeesService.getEmployeeByTeam(teamId);
        return ResponseEntity.ok(listEmployeesByTeam);
    }

    @GetMapping("/{roleId}/employees_by_role")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesByRole(@PathVariable Long roleId){
        List<EmployeesResponseDto> listEmployeesByRole = employeesService.getEmployeesByRole(roleId);
        return ResponseEntity.ok(listEmployeesByRole);
    }

    @GetMapping("/{teamId}/pm")
    public ResponseEntity<EmployeesResponseDto> getPmByTeam(@PathVariable Long teamId){
        EmployeesResponseDto PmByTeam = employeesService.getPmByTeam(teamId);
        return ResponseEntity.ok(PmByTeam);
    }

    @GetMapping("/all_pm")
    public ResponseEntity<List<EmployeesResponseDto>> getAllPm(){
        List<EmployeesResponseDto> listAllPm = employeesService.getAllPm();
        return ResponseEntity.ok(listAllPm);
    }

    @PutMapping("{employeeId}")
    public ResponseEntity<EmployeesResponseDto> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeesRequestDto employeesRequestDto){
        EmployeesResponseDto updatedEmployee = employeesService.updateEmployee(employeeId, employeesRequestDto);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId){
        EmployeesResponseDto deletedEmployee = employeesService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee Deleted!");
    }
}
