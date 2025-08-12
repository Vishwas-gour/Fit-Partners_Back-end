package com.fitPartner.service;


import com.fitPartner.entity.user.MyUser;
import com.fitPartner.repository.MyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final JavaMailSender mailSender;
    private final Map<String, String> otpStorage = new HashMap<> ();
    private final MyUserRepository myUserRepository;


    public ResponseEntity<?> generateAndSendOtp(String email, String subject) {
        System.out.println(subject);

        Optional<MyUser> user = myUserRepository.findByEmail(email);

        // ❌ Don't send OTP if subject is not register and user doesn't exist
        if (!"register".equals(subject) && user.isEmpty()) {
            return ResponseEntity.badRequest().body("You don't have an account");
        }

        // ❌ Don't send OTP if subject is register and user already exists
        if ("register".equals(subject) && user.isPresent()) {
            return ResponseEntity.badRequest().body("This email is already registered");
        }

        // ✅ Else, generate OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp);

        System.out.println(email + " - OTP: " + otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);

        return ResponseEntity.ok("OTP sent to your email!");
    }


    public ResponseEntity<?> verifyOtp(String email, String otp) {
        System.out.println ("verifyOtp in service");
        String validOtp = otpStorage.get (email);
        System.out.println (" ==> : " + validOtp + "<->" + otp);
        if (validOtp != null && validOtp.equals (otp)) {
            otpStorage.remove (email);
            return ResponseEntity.ok ("varification-successfully");
        }
        return ResponseEntity.badRequest ().body ("varification-failed");
    }
}

