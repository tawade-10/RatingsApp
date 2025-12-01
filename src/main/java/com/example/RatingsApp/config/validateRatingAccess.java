//package com.example.RatingsApp.config;
//
//import com.example.RatingsApp.entity.enums.RatingRoles;
//import com.example.RatingsApp.exception.APIException;
//
//public class validateRatingAccess {
//
//    RatingRoles ratingRole = rating.getRatingRole();
//
//    Long loggedInRole = loggedIn.getRole().getRoleId();
//
//    Long PM = 2L;
//    Long TL = 3L;
//    Long EMP = 4L;
//
//    // 1. EMPLOYEE (INDIVIDUAL)
//    if (loggedInRole.equals(EMP)) {
//        if (!ratingRole.equals(RatingRoles.SELF)) {
//            throw new APIException("Employees can access only their SELF ratings.");
//        }
//        if (!loggedIn.getEmployeeId().equals(rating.getEmployee().getEmployeeId())) {
//            throw new APIException("You can access only your own ratings.");
//        }
//    }
//
//    // 2. TL
//    if (loggedInRole.equals(TL)) {
//        if (!(ratingRole.equals(RatingRoles.SELF) ||
//                ratingRole.equals(RatingRoles.TL_TO_EMPLOYEE))) {
//            throw new APIException("TL can access only SELF and TL_TO_EMPLOYEE ratings.");
//        }
//        // TL can only see ratings inside his own team
//        if (!loggedIn.getTeam().getTeamId().equals(
//                rating.getEmployee().getTeam().getTeamId())) {
//            throw new APIException("TL can access only ratings within his team.");
//        }
//    }
//
//    // 3. PM
//    if (loggedInRole.equals(PM)) {
//        // PM can access anything inside his team
//        if (!loggedIn.getTeam().getTeamId().equals(
//                rating.getEmployee().getTeam().getTeamId())) {
//            throw new APIException("PM can access only ratings within his project.");
//        }
//    }
//}
