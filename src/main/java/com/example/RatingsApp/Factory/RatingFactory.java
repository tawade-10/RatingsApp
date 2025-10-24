package com.example.RatingsApp.Factory;

import com.example.RatingsApp.strategyimpl.PmToEmployeeRating;
import com.example.RatingsApp.strategyimpl.PmToTlRating;
import com.example.RatingsApp.strategyimpl.SelfRating;
import com.example.RatingsApp.strategy.RatingStrategy;
import org.springframework.stereotype.Component;

@Component
public class RatingFactory {

    private final SelfRating selfRating;

    private final PmToEmployeeRating pmToEmployeeRating;

    private final PmToTlRating pmToTlRating;

    public RatingFactory(SelfRating selfRating, PmToEmployeeRating pmToEmployeeRating, PmToTlRating pmToTlRating) {
        this.selfRating = selfRating;
        this.pmToEmployeeRating = pmToEmployeeRating;
        this.pmToTlRating = pmToTlRating;
    }

    public RatingStrategy getStrategy(String role) {
        if (role.equalsIgnoreCase("SELF")) {
            return selfRating;
        }else if(role.equalsIgnoreCase("PM_TO_EMPLOYEE")){
            return pmToEmployeeRating;
        }else if(role.equalsIgnoreCase("PM_TO_TL")){
            return pmToTlRating;
        }
        throw new IllegalArgumentException("No rating strategy found for role: " + role);
    }
}
