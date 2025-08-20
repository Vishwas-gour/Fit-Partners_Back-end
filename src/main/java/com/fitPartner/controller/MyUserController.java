package com.fitPartner.controller;

import com.fitPartner.service.MyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MyUserController {

    private final MyUserService myUserService;
    @GetMapping("/home")
    public void adminDashboard(){
        System.out.println ("Admin dashboard");
    }
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {

        return myUserService.getProfile (principal.getName ());
    }
    @GetMapping("/profile/{email}")
    public ResponseEntity<?> getProfileById(@PathVariable String email) {

        return myUserService.getProfile (email);
    }

    @PutMapping("/toggle-block/{email}")
    public ResponseEntity<?> toggleBlock(@PathVariable String email) {
        return myUserService.toggleBlock (email);

    }

}
