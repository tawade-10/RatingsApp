package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesRepo extends JpaRepository<Employees,Long> {
}
