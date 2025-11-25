package com.example.RatingsApp.config;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {

        String prefix = "";

        if (object instanceof com.example.RatingsApp.entity.Roles) {
            prefix = "ROL-";
        } else if (object instanceof com.example.RatingsApp.entity.Employees) {
            prefix = "EMP-";
        } else if (object instanceof com.example.RatingsApp.entity.Teams) {
            prefix = "T-";
        } else if (object instanceof com.example.RatingsApp.entity.Ratings) {
            prefix = "RAT-";
        } else if (object instanceof com.example.RatingsApp.entity.RatingsCycle) {
            prefix = "RC-";
        }

        long time = System.currentTimeMillis();

        return prefix + time;
    }
}
