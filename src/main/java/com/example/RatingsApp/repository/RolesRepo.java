package com.example.RatingsApp.repository;

import com.example.RatingsApp.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepo extends JpaRepository<Roles,Long> {
    Optional<Roles> findByRoleNameIgnoreCase(String roleName);

    Optional<Roles> findByRoleIdIgnoreCase(String roleId);
}
