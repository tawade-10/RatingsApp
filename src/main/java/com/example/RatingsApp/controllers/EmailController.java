package com.example.RatingsApp.controllers;

import com.example.RatingsApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-otp")
    public ResponseEntity<String> sendOtp() {
        try {
            emailService.sendOtpToAdmin();
            return ResponseEntity.ok("OTP sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending OTP: " + e.getMessage());
        }
    }
}
