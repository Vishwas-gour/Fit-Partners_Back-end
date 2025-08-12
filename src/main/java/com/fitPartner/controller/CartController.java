package com.fitPartner.controller;


import com.fitPartner.dto.CartItemResponse;
import com.fitPartner.entity.user.MyUser;
import com.fitPartner.repository.MyUserRepository;
import com.fitPartner.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private  final CartItemService cartItemService;
    private final MyUserRepository userRepo;
    // Add to cart
    @PostMapping("/add/{shoeId}/{selectedSize}")
    public ResponseEntity<String> addToCart(@PathVariable Long shoeId, @PathVariable int selectedSize, Principal principal) {
        return  cartItemService.addToCart(shoeId,selectedSize,principal);
    }

    @GetMapping
    private  ResponseEntity<List<CartItemResponse>>  getCartItem(Principal principal){
        return cartItemService.getCart (principal);
    }

    @PutMapping("/{cartItemId}/quantity")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity) {
        cartItemService.updateCartItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok("Quantity updated");
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.ok("Item removed");
    }
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Principal principal) {
        long  id = userRepo.findByEmail(principal.getName()).orElseThrow().getId ();
        return cartItemService.clearCart(id);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount(Principal principal) {
        long  id = userRepo.findByEmail(principal.getName()).orElseThrow().getId ();
        return cartItemService.getCartItemCount(id);
    }

    @PutMapping("/{cartItemId}/size")
    public ResponseEntity<String> updateCartItemSize(
            @PathVariable Long cartItemId,
            @RequestParam String size) {
        return cartItemService.updateCartItemSize(cartItemId, size);
    }
}
