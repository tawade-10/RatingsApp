package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.Roles;
import com.example.RatingsApp.entity.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeesRepo extends JpaRepository<Employees,Long> {

    Optional<Employees> findByName(String name);

    Optional<Employees> findByEmail(String email);

    List<Employees> findByTeam(Teams team);

    List<Employees> findByRole(Roles role);

    //Optional<UserDetails> findByUsername(String email);
}
