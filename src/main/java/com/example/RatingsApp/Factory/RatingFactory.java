package com.example.RatingsApp.Factory;

import com.example.RatingsApp.strategyimpl.SelfRating;
import com.example.RatingsApp.strategy.RatingStrategy;
import com.example.RatingsApp.strategyimpl.pmratings.PmToTTLRating;
import com.example.RatingsApp.strategyimpl.tlratings.TLToIndividualRating;
import com.example.RatingsApp.strategyimpl.ttlratings.TTLToTLRating;
import org.springframework.stereotype.Component;

@Component
public class RatingFactory {

      private final SelfRating selfRating;

      private final PmToTTLRating pmToTTLRating;

      private final TTLToTLRating ttlToTLRating;

      private final TLToIndividualRating tlToIndividualRating;

    public RatingFactory(SelfRating selfRating, PmToTTLRating pmToTTLRating, TTLToTLRating ttlToTLRating, TLToIndividualRating tlToIndividualRating) {
        this.selfRating = selfRating;
        this.pmToTTLRating = pmToTTLRating;
        this.ttlToTLRating = ttlToTLRating;
        this.tlToIndividualRating = tlToIndividualRating;
    }

    public RatingStrategy getStrategy(String role) {
        if (role.equalsIgnoreCase("SELF")) {
            return selfRating;
        }else if(role.equalsIgnoreCase("PM_TO_TTL")) {
            return pmToTTLRating;
        }else if(role.equalsIgnoreCase("TTL_TO_TL")){
            return ttlToTLRating;
        }else if(role.equalsIgnoreCase("TL_TO_EMPLOYEE")){
            return tlToIndividualRating;
        }
        throw new IllegalArgumentException("No rating strategy found for role: " + role);
    }
}
