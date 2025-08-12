package com.fitPartner.dto;

import com.fitPartner.entity.shoe.Color;
import com.fitPartner.entity.shoe.Size;
import com.fitPartner.entity.user.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CartItemResponse {
    private Long id;
    private int quantity;
    private LocalDateTime createdAt;

    private Long shoeId;
    private String shoeName;
    private String imageUrl;
    private String brand;

    private String description;
    private double salePrice;
    private int discountPercent;
    private double salePriceWithDiscount;
    private double cartPrice;
    private String category;
    private String gender;
    private String material;
    private int stock;
    private List<String>  color;
    private List<Integer> size;
    private int selectedSize;


    public CartItemResponse() {
    }

    public CartItemResponse(CartItem item) {
        this.id = item.getId ();
        this.quantity = item.getQuantity ();
        this.createdAt = item.getCreatedAt ();

        this.shoeId = item.getShoe ().getId ();
        this.shoeName = item.getShoe ().getName ();
        this.description = item.getShoe ().getDescription ();
        this.brand = item.getShoe ().getBrand ();
        this.stock = item.getShoe ().getStock ();
        this.selectedSize = item.getSelectedSize ();


        this.salePrice = item.getShoe ().getSalePrice ();
        this.discountPercent = (int) (item.getShoe ().getDiscountParentage ());
        this.salePriceWithDiscount = (int) (item.getShoe ().getPriceAfterDiscount ());
        this.cartPrice = item.getQuantity () * this.getSalePriceWithDiscount ();

        this.color = item.getShoe ().getColors().stream().map(Color::getColorName).toList();
        this.size = item.getShoe ().getSizes().stream().map(Size::getSizeNo).toList();
        this.category = item.getShoe ().getCategory ();
        this.gender = item.getShoe ().getGender ();
        this.material = item.getShoe ().getMaterial ();

        if (item.getShoe ().getImages () != null && !item.getShoe ().getImages ().isEmpty ()) {
            this.imageUrl = item.getShoe ().getImages ().get (0).getImgUrl ();
        }
    }
}
