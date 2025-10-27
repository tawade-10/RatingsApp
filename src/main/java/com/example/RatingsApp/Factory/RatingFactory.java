package com.example.RatingsApp.Factory;

import com.example.RatingsApp.strategyimpl.pmratings.PmToEmployeeRating;
import com.example.RatingsApp.strategyimpl.pmratings.PmToTLRating;
import com.example.RatingsApp.strategyimpl.SelfRating;
import com.example.RatingsApp.strategy.RatingStrategy;
import com.example.RatingsApp.strategyimpl.tlratings.TLToEmployeeRating;
import com.example.RatingsApp.strategyimpl.ttlratings.TTLToEmployeeRating;
import com.example.RatingsApp.strategyimpl.ttlratings.TTLToTLRating;
import org.springframework.stereotype.Component;

@Component
public class RatingFactory {

    private final SelfRating selfRating;

    private final PmToEmployeeRating pmToEmployeeRating;

    private final PmToTLRating pmToTlRating;

    private final TTLToEmployeeRating ttlToEmployeeRating;

    private final TTLToTLRating ttlToTLRating;

    private final TLToEmployeeRating tlToEmployeeRating;

    public RatingFactory(SelfRating selfRating, PmToEmployeeRating pmToEmployeeRating, PmToTLRating pmToTlRating, TTLToEmployeeRating ttlToEmployeeRating, TTLToTLRating ttlToTLRating, TLToEmployeeRating tlToEmployeeRating) {
        this.selfRating = selfRating;
        this.pmToEmployeeRating = pmToEmployeeRating;
        this.pmToTlRating = pmToTlRating;
        this.ttlToEmployeeRating = ttlToEmployeeRating;
        this.ttlToTLRating = ttlToTLRating;
        this.tlToEmployeeRating = tlToEmployeeRating;
    }

    public RatingStrategy getStrategy(String role) {
        if (role.equalsIgnoreCase("SELF")) {
            return selfRating;
        }else if(role.equalsIgnoreCase("PM_TO_EMPLOYEE")){
            return pmToEmployeeRating;
        }else if(role.equalsIgnoreCase("PM_TO_TL")){
            return pmToTlRating;
        }else if(role.equalsIgnoreCase("TTL_TO_EMPLOYEE")){
            return ttlToEmployeeRating;
        }else if(role.equalsIgnoreCase("TTL_TO_TL")){
            return ttlToTLRating;
        }else if(role.equalsIgnoreCase("TL_TO_EMPLOYEE")){
            return tlToEmployeeRating;
        }
        throw new IllegalArgumentException("No rating strategy found for role: " + role);
    }
}
