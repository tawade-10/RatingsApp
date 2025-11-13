//package com.example.RatingsApp.strategyimpl.ttlratings;
//
//import com.example.RatingsApp.dto.RatingsDto.RatingsRequestDto;
//import com.example.RatingsApp.entity.Employees;
//import com.example.RatingsApp.entity.Ratings;
//import com.example.RatingsApp.exception.APIException;
//import com.example.RatingsApp.exception.ResourceNotFoundException;
//import com.example.RatingsApp.repository.EmployeesRepo;
//import com.example.RatingsApp.strategy.RatingStrategy;
//import org.springframework.stereotype.Component;
//
//@Component("TTL_TO_TL")
//public class TTLToTLRating implements RatingStrategy {
//
//    private final EmployeesRepo employeesRepo;
//
//    public TTLToTLRating(EmployeesRepo employeesRepo) {
//        this.employeesRepo = employeesRepo;
//    }
//
//    @Override
//    public Ratings giveRating(RatingsRequestDto ratingsRequestDto) {
//
//        String ttlId = ratingsRequestDto.getRated_by_id();
//        String tlId = ratingsRequestDto.getEmployee_id();
//
//        if(ttlId == null || tlId == null){
//            throw new APIException("Both TTL ID and TL ID must be provided.");
//        }
//
//        Employees ttl = employeesRepo.findByEmployeeIdIgnoreCase(ttlId)
//                .orElseThrow(() -> new ResourceNotFoundException("TTL not found with ID: " + ttlId));
//
//        if(ttl.getRole() == null || !"R102".equalsIgnoreCase(ttl.getRole().getRoleId())){
//            throw new APIException("Only TTL can give this rating.");
//        }
//
//        Employees tl = employeesRepo.findByEmployeeIdIgnoreCase(tlId)
//                .orElseThrow(() -> new ResourceNotFoundException("TL not found with ID: " + tlId));
//
//        if(tl.getRole() == null || !"R103".equalsIgnoreCase(tl.getRole().getRoleId())){
//            throw new APIException("Only TL can receive this rating.");
//        }
//
//        Ratings ratings = new Ratings();
//
//       // ratings.setRatingRole(ratingsRequestDto.getRating_role());
//        ratings.setRatingValue(ratingsRequestDto.getRating_value());
//
//        return ratings;
//    }
//}
