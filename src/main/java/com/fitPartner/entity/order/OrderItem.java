package com.fitPartner.entity.order;

import com.fitPartner.dto.CartItemResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shoeId;
    private String shoeName;
    private String imageUrl;
    private String brand;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private int quantity;
    private double salePrice;
    private double discountPercent;
    private double salePriceWithDiscount;
    private double cartPrice;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders order;



    public OrderItem(CartItemResponse cartItem, Orders order) {
        this.shoeId = cartItem.getShoeId();
        this.shoeName = cartItem.getShoeName();
        this.imageUrl = cartItem.getImageUrl();
        this.brand = cartItem.getBrand();
        this.description = cartItem.getDescription();
        this.quantity = cartItem.getQuantity();
        this.salePrice = cartItem.getSalePrice();
        this.discountPercent = cartItem.getDiscountPercent();
        this.salePriceWithDiscount = cartItem.getSalePriceWithDiscount();
        this.cartPrice = cartItem.getCartPrice();
        this.order = order;
    }

}
