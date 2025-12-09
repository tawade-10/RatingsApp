package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Ratings;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.entity.Teams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeesRepo extends JpaRepository<Employees,Long> {

    Optional<Employees> findByName(String name);

    Page<Employees> findByName(String name, Pageable pageable);

    List<Employees> findByTeam(Teams team);

    List<Employees> findByRole(Roles role);

//    Optional<Employees> findByEmployeeIdIgnoreCase(String employeeId);

    Optional<Employees> findByEmail(String email);

    Optional<Employees> findByEmployeeId(String pmId);
}
