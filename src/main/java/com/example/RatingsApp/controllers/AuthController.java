package com.example.RatingsApp.controllers;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesRequestDto;
import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.facade.employees.EmployeesFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

//    private final JwtUtils jwtUtils;

    private final EmployeesFacade employeesFacade;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public AuthController(EmployeesFacade employeesFacade) {
        this.employeesFacade = employeesFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<EmployeesResponseDto> registerEmployee(@Valid @RequestBody EmployeesRequestDto employeesRequestDto) {
        EmployeesResponseDto registeredEmployee = employeesFacade.createEmployee(employeesRequestDto);
        employeesRequestDto.setPassword(encoder.encode(employeesRequestDto.getPassword()));
        return new ResponseEntity<>(registeredEmployee, HttpStatus.CREATED);
    }

//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
//        String email = loginRequest.get("email");
//        String password = loginRequest.get("password");
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(email, password)
//            );
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", "Invalid email or password"));
//        }
//
//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
//        String jwtToken = jwtUtils.createToken(new HashMap<>(), userDetails.getUsername());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("token", jwtToken);
//        response.put("email", email);
//
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/login")
    public String login(@RequestBody EmployeesRequestDto employeesRequestDto){
        return employeesFacade.verify(employeesRequestDto);
    }
}
