package com.fitPartner.repository;

import com.fitPartner.entity.user.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    Integer countByUserId(long id);
}
