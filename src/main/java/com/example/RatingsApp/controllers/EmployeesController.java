package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.facade.employees.EmployeesFacade;
import com.example.RatingsApp.service.Employees.EmployeesService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

    @PutMapping("/team/{teamId}")
    public ResponseEntity<EmployeesResponseDto> addEmployeeToTeam(@PathVariable Long teamId, @RequestBody EmployeesRequestDto employeesRequestDto){
        EmployeesResponseDto addedEmployee = employeesFacade.addEmployeeToTeam(employeesRequestDto,teamId);
        return new ResponseEntity<>(addedEmployee,HttpStatus.OK);
    }

    @GetMapping("{employeeId}")
    public ResponseEntity<EmployeesResponseDto> getEmployeeById(@PathVariable Long employeeId){
        EmployeesResponseDto getEmployee = employeesFacade.getEmployeeById(employeeId);
        return ResponseEntity.ok(getEmployee);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeesResponseDto>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<EmployeesResponseDto> employees = employeesFacade.getAllEmployees(page, size);
        return ResponseEntity.ok(employees);
    }


    @GetMapping("/employees_by_name")
    public ResponseEntity<Page<EmployeesResponseDto>> getEmployeesByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam String name
    ) {
        Page<EmployeesResponseDto> listEmployeesByName = employeesFacade.getEmployeeByName(page, size, name);
        return ResponseEntity.ok(listEmployeesByName);
    }


    @GetMapping("/employees_by_team/{teamId}")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesByTeam(@PathVariable Long teamId){
        List<EmployeesResponseDto> listEmployeesByTeam = employeesFacade.getEmployeeByTeam(teamId);
        return ResponseEntity.ok(listEmployeesByTeam);
    }

    @GetMapping("/employees_by_role/{roleId}")
    public ResponseEntity<List<EmployeesResponseDto>> getEmployeesByRole(@PathVariable Long roleId){
        List<EmployeesResponseDto> listEmployeesByRole = employeesFacade.getEmployeesByRole(roleId);
        return ResponseEntity.ok(listEmployeesByRole);
    }

    @GetMapping("/pm/{teamId}")
    public ResponseEntity<EmployeesResponseDto> getPmByTeam(@PathVariable Long teamId){
        EmployeesResponseDto PmByTeam = employeesFacade.getPmByTeam(teamId);
        return ResponseEntity.ok(PmByTeam);
    }

    @GetMapping("/all_pm")
    public ResponseEntity<List<EmployeesResponseDto>> getAllPm(){
        List<EmployeesResponseDto> listAllPm = employeesFacade.getAllPm();
        return ResponseEntity.ok(listAllPm);
    }

    @PutMapping("{employeeId}")
    public ResponseEntity<EmployeesResponseDto> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeesRequestDto employeesRequestDto){
        EmployeesResponseDto updatedEmployee = employeesFacade.updateEmployee(employeeId, employeesRequestDto);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @PutMapping("/change-team")
    public ResponseEntity<EmployeesResponseDto> changeTeam(@RequestBody EmployeesRequestDto employeesRequestDto, Long employeeId){
        EmployeesResponseDto updatedTeam = employeesFacade.changeTeam(employeesRequestDto,employeeId);
        return new ResponseEntity<>(updatedTeam,HttpStatus.OK);
    }

    @PutMapping("/change-role")
    public ResponseEntity<EmployeesResponseDto> changeRole(@RequestBody EmployeesRequestDto employeesRequestDto, Long employeeId, Long roleId){
        EmployeesResponseDto updatedRole = employeesFacade.changeRole(employeesRequestDto,employeeId,roleId);
        return new ResponseEntity<>(updatedRole,HttpStatus.OK);
    }

    @DeleteMapping("{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long employeeId){
        EmployeesResponseDto deletedEmployee = employeesFacade.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee Deleted!");
    }
}
