package com.fitPartner.service;

import com.fitPartner.dto.WishlistItemResponse;
import com.fitPartner.entity.shoe.Shoe;
import com.fitPartner.entity.user.MyUser;
import com.fitPartner.entity.user.WishlistItem;
import com.fitPartner.repository.CartItemRepository;
import com.fitPartner.repository.MyUserRepository;
import com.fitPartner.repository.ShoesRepository;
import com.fitPartner.repository.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final ShoesRepository shoesRepo;
    private final MyUserRepository userRepo;
    private final WishlistItemRepository wishlistItemRepo;
    private final CartItemRepository cartItemRepo;

    public ResponseEntity<String> addToWishList(long shoeId, Principal principal) {
        Shoe shoe = shoesRepo.findById (shoeId).orElseThrow (() -> new RuntimeException ("Shoe not found"));
        MyUser user = userRepo.findByEmail (principal.getName ()).orElseThrow (() -> new UsernameNotFoundException ("User not found"));

        boolean alreadyInWishlist = user.getWishlistItems ().stream ().anyMatch (item -> item.getShoe ().getId ().equals (shoeId));

        if (alreadyInWishlist) return ResponseEntity.badRequest ().body ("Shoe is already in wishlist");


        WishlistItem wish = new WishlistItem ();
        wish.setShoe (shoe);
        wish.setUser (user);
        wishlistItemRepo.save (wish);

        return ResponseEntity.ok ("Added to wishlist");
    }


    public ResponseEntity<List<WishlistItemResponse>> getWishlistItems(Principal principal) {
        MyUser user = userRepo.findByEmail (principal.getName ()).orElseThrow ();

        List<WishlistItemResponse> dtoList = user.getWishlistItems ().stream ()
                .map (WishlistItemResponse::new)
                .toList ();

        return ResponseEntity.ok (dtoList);
    }


    public void removeWishlistItem(Long wishlistItemId) {
        wishlistItemRepo.deleteById (wishlistItemId);
    }

    public ResponseEntity<Integer> getWishlistCount(Principal principal) {
       long id = userRepo.findByEmail (principal.getName ()).orElseThrow ().getId ();
        return ResponseEntity.ok (wishlistItemRepo.countByUserId (id));
    }
}
