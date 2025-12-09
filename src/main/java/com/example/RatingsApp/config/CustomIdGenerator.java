package com.example.RatingsApp.config;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {

        String prefix = "";
        String query = "";

        // For Roles
        if (object instanceof com.example.RatingsApp.entity.Roles) {
            prefix = "ROL";
            query = "SELECT role_id FROM roles WHERE role_id LIKE 'ROL%' ORDER BY role_id DESC LIMIT 1";
        }

        // For Employees
        if (object instanceof com.example.RatingsApp.entity.Employees) {
            prefix = "EMP";
            query = "SELECT employee_id FROM employee WHERE employee_id LIKE 'EMP%' ORDER BY employee_id DESC LIMIT 1";
        }

        // For Teams
        if (object instanceof com.example.RatingsApp.entity.Teams) {
            prefix = "T";
            query = "SELECT team_id FROM team WHERE team_id LIKE 'T%' ORDER BY team_id DESC LIMIT 1";
        }

        // For Ratings
        if (object instanceof com.example.RatingsApp.entity.Ratings) {
            prefix = "RAT";
            query = "SELECT rating_id FROM ratings WHERE rating_id LIKE 'RAT%' ORDER BY rating_id DESC LIMIT 1";
        }

        // For Ratings
        if (object instanceof com.example.RatingsApp.entity.RatingsCycle) {
            prefix = "RC";
            query = "SELECT cycle_id FROM ratings_cycle WHERE cycle_id LIKE 'RC%' ORDER BY cycle_id DESC LIMIT 1";
        }

        // Get last ID from DB
        String lastId = (String) session.createNativeQuery(query).uniqueResult();

        int nextId = 1;

        if (lastId != null) {
            String numberPart = lastId.replace(prefix, "");
            nextId = Integer.parseInt(numberPart) + 1;
        }

        return prefix + String.format("%03d", nextId);
    }
}
