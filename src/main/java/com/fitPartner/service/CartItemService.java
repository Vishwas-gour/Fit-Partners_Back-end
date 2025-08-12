package com.fitPartner.service;

import com.fitPartner.dto.CartItemResponse;
import com.fitPartner.entity.shoe.Shoe;
import com.fitPartner.entity.user.CartItem;
import com.fitPartner.entity.user.MyUser;
import com.fitPartner.repository.CartItemRepository;
import com.fitPartner.repository.MyUserRepository;
import com.fitPartner.repository.ShoesRepository;
import com.fitPartner.repository.WishlistItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final ShoesRepository shoesRepo;
    private final MyUserRepository userRepo;
    private  final CartItemRepository cartItemRepo;
    public ResponseEntity<String> addToCart(Long shoeId, int size, Principal principal) {
        MyUser user = userRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException ("User not found"));

        Shoe shoe = shoesRepo.findById(shoeId)
                .orElseThrow(() -> new RuntimeException("Shoe not found"));

        // check if shoe already in cart
        boolean alreadyInCart = cartItemRepo.existsByUserAndShoe(user, shoe);

        if (alreadyInCart) {
            System.out.println ("alreadyInCart");
            return ResponseEntity.badRequest().body("Shoe is already in cart");
        }

        // else, add to cart
        CartItem cartItem = new CartItem();
        cartItem.setShoe(shoe);
        cartItem.setUser(user);
        cartItem.setQuantity (1);
        cartItem.setPrice (shoe.getCostPrice ());
        cartItem.setSelectedSize (size);
        cartItemRepo.save(cartItem);
        return ResponseEntity.ok("Shoe added to cart");
    }

    public ResponseEntity<List<CartItemResponse>> getCart(Principal principal) {
        MyUser user = userRepo.findByEmail(principal.getName()).orElseThrow();

        List<CartItemResponse> cartItemResponseDTOList = user.getCartItems().stream()
                .map(CartItemResponse::new)
                .toList();

        return ResponseEntity.ok(cartItemResponseDTOList);
    }


    public void updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        item.setQuantity(quantity);
        cartItemRepo.save(item);
    }

    public void removeCartItem(Long cartItemId) {
        cartItemRepo.deleteById(cartItemId);
    }
    @Transactional
    public ResponseEntity<?> clearCart(long id) {
            cartItemRepo.deleteByUserId(id);
        return ResponseEntity.ok("Cart cleared");
    }

    public ResponseEntity<Integer> getCartItemCount(long id) {
        int count = cartItemRepo.countByUserId(id);
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<String> updateCartItemSize(Long cartItemId, String size) {
        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        item.setSelectedSize(Integer.parseInt(size));
        cartItemRepo.save(item);
        return ResponseEntity.ok("Size updated");
    }
}
