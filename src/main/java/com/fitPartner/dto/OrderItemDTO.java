package com.fitPartner.dto;

import com.fitPartner.entity.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private Long shoeId;
    private String shoeName;
    private String imageUrl;
    private String brand;
    private String description;
    private int quantity;
    private double salePrice;
    private double discountPercent;
    private double salePriceWithDiscount;
    private double cartPrice;

    public OrderItemDTO(OrderItem item) {
        this.shoeId = item.getShoeId();
        this.shoeName = item.getShoeName();
        this.imageUrl = item.getImageUrl();
        this.brand = item.getBrand();
        this.description = item.getDescription();
        this.quantity = item.getQuantity();
        this.salePrice = item.getSalePrice();
        this.discountPercent = item.getDiscountPercent();
        this.salePriceWithDiscount = item.getSalePriceWithDiscount();
        this.cartPrice = item.getCartPrice();
    }

}
