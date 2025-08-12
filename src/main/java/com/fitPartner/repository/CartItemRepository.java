package com.fitPartner.repository;

import com.fitPartner.entity.shoe.Shoe;
import com.fitPartner.entity.user.CartItem;
import com.fitPartner.entity.user.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByUserAndShoe(MyUser user, Shoe shoe);

    List<CartItem> findByUserId(Long userId);

    void deleteByUserId(long id);

    int countByUserId(long id);
}
