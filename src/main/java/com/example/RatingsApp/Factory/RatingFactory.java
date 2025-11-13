package com.example.RatingsApp.Factory;

import com.example.RatingsApp.strategyimpl.pmratings.PmToIndividualRating;
import com.example.RatingsApp.strategyimpl.pmratings.PmToTLRating;
import com.example.RatingsApp.strategyimpl.SelfRating;
import com.example.RatingsApp.strategy.RatingStrategy;
// import com.example.RatingsApp.strategyimpl.pmratings.PmToTTLRating;
import com.example.RatingsApp.strategyimpl.tlratings.TLToIndividualRating;
//import com.example.RatingsApp.strategyimpl.ttlratings.TTLToEmployeeRating;
//import com.example.RatingsApp.strategyimpl.ttlratings.TTLToTLRating;
import org.springframework.stereotype.Component;

@Component
public class RatingFactory {

    private final SelfRating selfRating;

    private final PmToIndividualRating pmToIndividualRating;

    private final PmToTLRating pmToTlRating;

//    private final PmToTTLRating pmToTTLRating;
//
//    private final TTLToEmployeeRating ttlToEmployeeRating;
//
//    private final TTLToTLRating ttlToTLRating;

    private final TLToIndividualRating tlToIndividualRating;

    public RatingFactory(SelfRating selfRating, PmToIndividualRating pmToIndividualRating, PmToTLRating pmToTlRating, TLToIndividualRating tlToIndividualRating) {
        this.selfRating = selfRating;
        this.pmToIndividualRating = pmToIndividualRating;
        this.pmToTlRating = pmToTlRating;
//        this.pmToTTLRating = pmToTTLRating;
//        this.ttlToEmployeeRating = ttlToEmployeeRating;
//        this.ttlToTLRating = ttlToTLRating;
        this.tlToIndividualRating = tlToIndividualRating;
    }

    public RatingStrategy getStrategy(String role) {
        if (role.equalsIgnoreCase("SELF")) {
            return selfRating;
        }else if(role.equalsIgnoreCase("PM_TO_EMPLOYEE")){
            return pmToIndividualRating;
        }else if(role.equalsIgnoreCase("PM_TO_TL")) {
            return pmToTlRating;
//        } else if (role.equalsIgnoreCase("PM_TO_TTL")) {
//            return pmToTTLRating;
//        } else if(role.equalsIgnoreCase("TTL_TO_EMPLOYEE")){
//            return ttlToEmployeeRating;
//        }else if(role.equalsIgnoreCase("TTL_TO_TL")){
//            return ttlToTLRating;
        }else if(role.equalsIgnoreCase("TL_TO_EMPLOYEE")){
            return tlToIndividualRating;
        }
        throw new IllegalArgumentException("No rating strategy found for role: " + role);
    }
}
