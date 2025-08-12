package com.fitPartner.repository;

import com.fitPartner.entity.shoe.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByShoeId(Long shoeId);
}
