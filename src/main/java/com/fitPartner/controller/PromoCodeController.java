package com.fitPartner.controller;

import com.fitPartner.entity.shoe.PromoCode;
import com.fitPartner.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RequiredArgsConstructor
@RestController
@RequestMapping("/promocode")
public class PromoCodeController {

    private final PromoCodeRepository promoRepo;

    @GetMapping("/{code}")
    public ResponseEntity<?> checkPromo(@PathVariable String code) {
        Optional<PromoCode> promo = promoRepo.findByCode(code);
        if (promo.isPresent()) {
            return ResponseEntity.ok(promo.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid promo code");
    }
}
