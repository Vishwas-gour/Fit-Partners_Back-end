package com.fitPartner.dto;

import com.fitPartner.entity.shoe.Review;
import com.fitPartner.entity.shoe.Shoe;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ShoeResponse {
    private Long id;
    private String name;
    private String brand;
    private int stock;
    private String description;

    private double discountParentage;
    private double salePrice;
    private double costPrice;
    private double priceAfterDiscount;

    private String category;
    private String gender;
    private String material;
    private double weight;


    private List<String> imageUrls;
    private List<Integer> sizes;
    private List<String> colors;
    private List<Integer> ratings;
    private boolean available;


    public ShoeResponse(Shoe shoe) {
        this.id = shoe.getId ();
        this.name = shoe.getName ();
        this.brand = shoe.getBrand ();
        this.stock = shoe.getStock ();
        this.description = shoe.getDescription ();
        this.gender = shoe.getGender ();
        this.weight = shoe.getWeight ();
        this.material = shoe.getMaterial ();
        this.category = shoe.getCategory ();
        this.available = shoe.isAvailable ();

        this.salePrice = shoe.getSalePrice ();
        this.costPrice = shoe.getCostPrice ();
        this.discountParentage = shoe.getDiscountParentage ();
        this.priceAfterDiscount = shoe.getPriceAfterDiscount ();

        this.sizes = shoe.getSizes ().stream ().map (size -> size.getSizeNo ()).collect (Collectors.toList ());
        this.colors = shoe.getColors ().stream ().map (color -> color.getColorName ()).collect (Collectors.toList ());
        this.imageUrls = shoe.getImages ().stream ().map (img -> img.getImgUrl ()).collect (Collectors.toList ());
        this.ratings = shoe.getReviews().stream ().map(review -> review.getRating()).collect(Collectors.toList());
    }
}
