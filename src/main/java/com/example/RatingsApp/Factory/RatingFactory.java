package com.example.RatingsApp.Factory;

import com.example.RatingsApp.strategyimpl.SelfRating;
import com.example.RatingsApp.strategy.RatingStrategy;
import com.example.RatingsApp.strategyimpl.pmratings.PmToIndividualRating;
import com.example.RatingsApp.strategyimpl.pmratings.PmToTLRating;
import com.example.RatingsApp.strategyimpl.tlratings.TLToIndividualRating;
import org.springframework.stereotype.Component;

@Component
public class RatingFactory {

      private final SelfRating selfRating;

      private final PmToIndividualRating pmToIndividualRating;

      private final PmToTLRating pmToTLRating;

      private final TLToIndividualRating tlToIndividualRating;

    public RatingFactory(SelfRating selfRating,PmToIndividualRating pmToIndividualRating, PmToTLRating pmToTLRating, TLToIndividualRating tlToIndividualRating) {
        this.selfRating = selfRating;
        this.pmToIndividualRating = pmToIndividualRating;
        this.pmToTLRating = pmToTLRating;
        this.tlToIndividualRating = tlToIndividualRating;
    }

    public RatingStrategy getStrategy(String role) {
        if (role.equalsIgnoreCase("SELF")) {
            return selfRating;
        }else if(role.equalsIgnoreCase("TL_TO_INDIVIDUAL")){
            return tlToIndividualRating;
        }else if(role.equalsIgnoreCase("PM_TO_TL")){
            return pmToTLRating;
        }else if(role.equalsIgnoreCase("PM_TO_INDIVIDUAL")){
            return pmToIndividualRating;
        }
        throw new IllegalArgumentException("No rating strategy found for role: " + role);
    }
}
