package com.fitPartner.controller;

import com.fitPartner.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private  final AdminService adminService;

    @GetMapping("/findByRole")
    public ResponseEntity<?> findByRole(@RequestParam String role) {
        System.out.println ("role->   " + role   )  ;
        return adminService.findByRole(role);
    }
}
