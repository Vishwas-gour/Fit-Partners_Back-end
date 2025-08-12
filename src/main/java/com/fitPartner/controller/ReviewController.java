package com.fitPartner.controller;

import com.fitPartner.dto.ReviewResponse;
import com.fitPartner.entity.shoe.Review;
import com.fitPartner.entity.shoe.Shoe;
import com.fitPartner.entity.user.MyUser;
import com.fitPartner.repository.MyUserRepository;
import com.fitPartner.repository.ReviewRepository;
import com.fitPartner.repository.ShoesRepository;
import com.fitPartner.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{shoeId}")
    public ResponseEntity<?> addReview(@PathVariable Long shoeId,
                                            @RequestParam String comment,
                                            @RequestParam int rating,
                                            @RequestParam(required = false) List<MultipartFile> images,
                                            Principal principal) {
        return reviewService.addReview (shoeId, comment, rating, images, principal);
    }

    @GetMapping("/{shoeId}")
    public List<ReviewResponse> getReviews(@PathVariable Long shoeId) {
        System.out.println (reviewService.getReviews (shoeId));
        return reviewService.getReviews (shoeId);
    }

    @GetMapping("/{shoeId}/summary")
    public Map<String, Object> getReviewSummary(@PathVariable Long shoeId) {
        return reviewService.getReviewSummary (shoeId);
    }

    @GetMapping("/averageRating/{shoeId}")
    public double getAverageRating(@PathVariable Long shoeId) {
        System.out.println ("averageRating calling");
        return reviewService.getAverageRating (shoeId);

    }
}
