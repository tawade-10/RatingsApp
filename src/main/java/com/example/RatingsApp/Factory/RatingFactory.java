package com.example.RatingsApp.Factory;

import com.example.RatingsApp.strategyimpl.SelfRating;
import com.example.RatingsApp.strategy.RatingStrategy;
import com.example.RatingsApp.strategyimpl.pmratings.PMToTMRating;
import com.example.RatingsApp.strategyimpl.pmratings.PMToTLRating;
import com.example.RatingsApp.strategyimpl.tlratings.TLToTMRating;
import org.springframework.stereotype.Component;

@Component
public class RatingFactory {

      private final SelfRating selfRating;

      private final PMToTMRating PMToTMRating;

      private final PMToTLRating pmToTLRating;

      private final TLToTMRating tlToTMRating;

    public RatingFactory(SelfRating selfRating, PMToTMRating PMToTMRating, PMToTLRating pmToTLRating, TLToTMRating tlToTMRating) {
        this.selfRating = selfRating;
        this.PMToTMRating = PMToTMRating;
        this.pmToTLRating = pmToTLRating;
        this.tlToTMRating = tlToTMRating;
    }

    public RatingStrategy getStrategy(String role) {
        if (role.equalsIgnoreCase("SELF")) {
            return selfRating;
        }else if(role.equalsIgnoreCase("TL_TO_TM")){
            return tlToTMRating;
        }else if(role.equalsIgnoreCase("PM_TO_TL")){
            return pmToTLRating;
        }else if(role.equalsIgnoreCase("PM_TO_TM")){
            return PMToTMRating;
        }
        throw new IllegalArgumentException("No rating strategy found for role: " + role);
    }
}
