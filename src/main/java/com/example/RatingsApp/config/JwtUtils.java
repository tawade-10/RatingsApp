//package com.example.RatingsApp.config;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.Base64;
//import java.util.Date;
//import java.util.Map;
//
//@Service
//public class JwtUtils {
//
//    private static final String SECRET_KEY = "N2Y1ZWRmZjAtOTQ5Yy00Mjk3LWFkZTQtM2YzYmVjNGE0ZDY4MzMzMjY2Nzg4YWYz";
//
//    private SecretKey getSigningKey() {
//        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String createToken(Map<String, Object> claims, String username) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//}
