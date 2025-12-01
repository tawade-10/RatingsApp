//package com.example.RatingsApp.config;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserSession {
//
//    private static final ThreadLocal<LoggedInUser> loggedInUser = new ThreadLocal<>();
//
//    public static void setUser(LoggedInUser user) {
//        loggedInUser.set(user);
//    }
//
//    public static LoggedInUser getUser() {
//        return loggedInUser.get();
//    }
//
//    public static void clear() {
//        loggedInUser.remove();
//    }
//
//    public record LoggedInUser(Long userId, Long roleId) {}
//}
