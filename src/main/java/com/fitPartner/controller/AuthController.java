package com.fitPartner.controller;

import com.fitPartner.entity.user.MyUser;
import com.fitPartner.service.MyUserService;
import com.fitPartner.service.OtpService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MyUserService userService;
    private final  OtpService otpService;
    public AuthController(MyUserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        return userService.register (request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody MyUser user) {
        return userService.login (user);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> forgetPassword(@RequestBody Map<String, String> request) {
        return userService.forgetPassword (request.get("email"),request.get ("newPassword"));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request){
        return otpService.generateAndSendOtp (request.get ("email"), request.get ("subject"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request){
        return  otpService.verifyOtp (request.get ("email"), request.get ("otp"));
    }
}
