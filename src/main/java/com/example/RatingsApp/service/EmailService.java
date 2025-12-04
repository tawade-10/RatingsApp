package com.example.RatingsApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String ADMIN_EMAIL = "tawadeshubham10@gmail.com";

    private String lastOTP;
    private Instant otpTimestamp;

    public void sendOtpToAdmin() {

        String otp = String.format("%05d", new Random().nextInt(100000));
        this.lastOTP = otp;
        this.otpTimestamp = Instant.now();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(ADMIN_EMAIL);
        message.setTo(ADMIN_EMAIL);
        message.setSubject("OTP Verification");
        message.setText("Your OTP for verification: " + otp);

        mailSender.send(message);
    }
}
