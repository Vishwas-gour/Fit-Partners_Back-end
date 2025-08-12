package com.fitPartner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
   private   final MyUserService userService;
    public ResponseEntity<?> findByRole(String role) {
        return userService.findByRole(role);
    }
}
