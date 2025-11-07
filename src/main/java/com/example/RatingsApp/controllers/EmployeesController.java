package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.facade.employees.EmployeesFacade;
import com.example.RatingsApp.service.Employees.EmployeesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {

    private final EmployeesFacade employeesFacade;

    private final EmployeesService employeesService;

    public EmployeesController(EmployeesFacade employeesFacade, EmployeesService employeesService) {
        this.employeesFacade = employeesFacade;
        this.employeesService = employeesService;
    }

    @PostMapping
    public ResponseEntity<EmployeesResponseDto> createEmployee(@Valid @RequestBody EmployeesRequestDto employeesRequestDto) {
        EmployeesResponseDto savedEmployee = employeesFacade.createEmployee(employeesRequestDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("{employeeId}")
    public ResponseEntity<EmployeesResponseDto> getEmployeeById(@PathVariable String employeeId){
        EmployeesResponseDto getEmployee = employeesFacade.getEmployeeById(employeeId);
        return ResponseEntity.ok(getEmployee);
    }

    @GetMapping
    public ResponseEntity<List<EmployeesResponseDto>> getAllEmployees(){
        List<EmployeesResponseDto> listEmployees = employeesFacade.getAllEmployees();
        return ResponseEntity.ok(listEmployees);
    }

    @GetMapping("/employees_by_name")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesByName(@RequestParam String name){
        List<EmployeesResponseDto> listEmployeesByName = employeesFacade.getEmployeeByName(name);
        return ResponseEntity.ok(listEmployeesByName);
    }

    @GetMapping("/{teamId}/employees_by_team")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesByTeam(@PathVariable String teamId){
        List<EmployeesResponseDto> listEmployeesByTeam = employeesFacade.getEmployeeByTeam(teamId);
        return ResponseEntity.ok(listEmployeesByTeam);
    }

    @GetMapping("/{roleId}/employees_by_role")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesByRole(@PathVariable String roleId){
        List<EmployeesResponseDto> listEmployeesByRole = employeesFacade.getEmployeesByRole(roleId);
        return ResponseEntity.ok(listEmployeesByRole);
    }

    @GetMapping("/{teamId}/pm")
    public ResponseEntity<EmployeesResponseDto> getPmByTeam(@PathVariable String teamId){
        EmployeesResponseDto PmByTeam = employeesFacade.getPmByTeam(teamId);
        return ResponseEntity.ok(PmByTeam);
    }

    @GetMapping("/all_pm")
    public ResponseEntity<List<EmployeesResponseDto>> getAllPm(){
        List<EmployeesResponseDto> listAllPm = employeesFacade.getAllPm();
        return ResponseEntity.ok(listAllPm);
    }

    @PutMapping("{employeeId}")
    public ResponseEntity<EmployeesResponseDto> updateEmployee(@PathVariable String employeeId, @RequestBody EmployeesRequestDto employeesRequestDto){
        EmployeesResponseDto updatedEmployee = employeesFacade.updateEmployee(employeeId, employeesRequestDto);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String employeeId){
        EmployeesResponseDto deletedEmployee = employeesFacade.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee Deleted!");
    }
}
