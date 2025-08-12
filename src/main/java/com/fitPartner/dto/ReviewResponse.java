package com.fitPartner.dto;


import com.fitPartner.entity.shoe.Review;
import com.fitPartner.entity.shoe.ReviewImage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private int rating;
    private String comment;
    private String username;
    private LocalDateTime createdAt;
    private List<String> imageUrls;

    public ReviewResponse(Review r) {
        this.id = r.getId();
        this.rating = r.getRating();
        this.comment = r.getComment();
        this.createdAt = r.getCreatedAt();
        this.username = r.getUser().getUsername();
        this.imageUrls = r.getImages() != null
                ? r.getImages().stream().map(ReviewImage::getImageUrl).collect(Collectors.toList())
                : null;
    }
}
