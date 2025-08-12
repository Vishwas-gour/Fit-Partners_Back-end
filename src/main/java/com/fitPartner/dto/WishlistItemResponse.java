package com.fitPartner.dto;

import com.fitPartner.entity.user.WishlistItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WishlistItemResponse {
    private Long id;
    private LocalDateTime createdAt;
    private Long shoeId;
    private String shoeName;
    private String imageUrl;
    private String descriptions;
    private double salePrice;
    private int discountPercent;
    private int salePriceWithDiscount;
    private String category;
    private String brand;
    private int stock;

    public WishlistItemResponse(WishlistItem item) {
        this.id = item.getId();
        this.createdAt = item.getCreatedAt();
        this.shoeId = item.getShoe().getId();
        this.shoeName = item.getShoe().getName();
        this.descriptions = item.getShoe ().getDescription ();
        this.salePrice = item.getShoe().getSalePrice();
        this.discountPercent = (int) (item.getShoe().getDiscountParentage());
        this.salePriceWithDiscount = (int) (item.getShoe().getPriceAfterDiscount());
        this.category = item.getShoe().getCategory();
        this.brand = item.getShoe().getBrand();
        this.stock = item.getShoe().getStock();


        if (item.getShoe().getImages() != null && !item.getShoe().getImages().isEmpty()) {
            this.imageUrl = item.getShoe().getImages().get(0).getImgUrl();
        }
    }

}
