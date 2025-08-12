package com.fitPartner.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fitPartner.dto.ReviewResponse;
import com.fitPartner.entity.shoe.Review;
import com.fitPartner.entity.shoe.ReviewImage;
import com.fitPartner.entity.shoe.Shoe;
import com.fitPartner.entity.user.MyUser;
import com.fitPartner.repository.MyUserRepository;
import com.fitPartner.repository.ReviewImageRepository;
import com.fitPartner.repository.ReviewRepository;
import com.fitPartner.repository.ShoesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepo;
    private final MyUserRepository userRepo;
    private final ShoesRepository shoeRepo;
    private final Cloudinary cloudinary;
    private final ReviewImageRepository reviewImageRepo;

    public Map<String, Object> getReviewSummary(Long shoeId) {
        List<Review> reviews = reviewRepo.findByShoeId (shoeId);

        int totalReviews = reviews.size ();
        double averageRating = 0.0;

        if (totalReviews > 0) {
            averageRating = reviews.stream ()
                    .mapToInt (Review::getRating)
                    .average ()
                    .orElse (0.0);
        }

        Map<String, Object> response = new HashMap<> ();
        response.put ("totalReviews", totalReviews);
        response.put ("averageRating", Math.round (averageRating * 10.0) / 10.0); // round to 1 decimal
        return response;
    }

    public List<ReviewResponse> getReviews(Long shoeId) {
        System.out.println (reviewRepo.findByShoeId (shoeId));
        List<Review> reviews = reviewRepo.findByShoeId (shoeId);
        return reviews.stream ().map (ReviewResponse::new).collect (Collectors.toList ());
    }


    public ResponseEntity<?> addReview(Long shoeId, String comment, int rating, List<MultipartFile> images, Principal principal) {
        try {
            MyUser user = userRepo.findByEmail(principal.getName()).orElseThrow();
            Shoe shoe = shoeRepo.findById(shoeId).orElseThrow();

            Review review = new Review();
            review.setUser(user);
            review.setRating(rating);
            review.setComment(comment);
            review.setShoe(shoe);
            reviewRepo.save(review); // save review first (needed for FK)

            List<ReviewImage> reviewImages = new ArrayList<> ();

            if(images != null && !images.isEmpty ()){
                for (MultipartFile file : images) {
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of("folder", "Reviews-Images"));
                    String url = uploadResult.get("secure_url").toString();

                    ReviewImage ri = new ReviewImage(url);
                    ri.setReview(review); // link to review
                    reviewImageRepo.save(ri); // save image
                    reviewImages.add(ri); // collect for setting back if needed
                }
            }

            review.setImages(reviewImages); // link images back (optional if cascade works)
            reviewRepo.save(review);

            return ResponseEntity.ok("Review added");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public double getAverageRating(Long shoeId) {
        List<Review> reviews = reviewRepo.findByShoeId (shoeId);
        double averageRating = 0.0;

        if (reviews.size () > 0) {
            averageRating = reviews.stream ()
                    .mapToDouble (Review::getRating)
                    .average ()
                    .orElse (0.0);
        }

        return Math.round (averageRating * 10.0) / 10.0; // round to 1 decimal
    }
}
