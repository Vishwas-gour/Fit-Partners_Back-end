package com.fitPartner.controller;


import com.fitPartner.dto.WishlistItemResponse;

import com.fitPartner.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private  final WishlistService wishlistService;

    @PostMapping("/add/{shoeId}")
    public ResponseEntity<String> addToWishlist(@PathVariable long shoeId, Principal principal) {
      return  wishlistService.addToWishList(shoeId,principal);
    }

    @GetMapping
    public  ResponseEntity<List<WishlistItemResponse>> getWishlistItems(Principal principal) {
        return  wishlistService.getWishlistItems(principal);
    }

    @DeleteMapping("/{wishlistItemId}")
    public ResponseEntity<?> removeWishlistItem(@PathVariable Long wishlistItemId) {
        wishlistService.removeWishlistItem (wishlistItemId);
        return ResponseEntity.ok("Item removed");
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getWishlistCount(Principal principal) {
        return  wishlistService.getWishlistCount(principal);
    }

}
