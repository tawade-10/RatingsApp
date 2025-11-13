package com.example.RatingsApp.service.Security;

import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.repository.EmployeesRepo;
import com.example.RatingsApp.repository.RolesRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner createAdminUser(RolesRepo rolesRepo,EmployeesRepo employeesRepo, PasswordEncoder passwordEncoder){
        return args -> {
            // Check if an Admin user already exists
            if (employeesRepo.findByEmail("admin123@gmail.com").isEmpty()) {
                Roles pmRole = rolesRepo.findByRoleIdIgnoreCase("R100")
                        .orElseGet(() -> {
                            Roles role = new Roles();
                            role.setRoleId("R100");
                            role.setRoleName("Admin");
                            return rolesRepo.save(role);
                        });
                Employees admin = new Employees();
                admin.setEmployeeId("EMP001");
                admin.setName("Admin User");
                admin.setEmail("admin123@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(pmRole);
                admin.setTeam(null);

                employeesRepo.save(admin);
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }
}
