package com.fitPartner.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@Data
public class ShoeRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private int stock;

    private double costPrice;
    private double salePrice;
    private double discountParentage;
    private double priceAfterDiscount;

    private String category;
    private String gender;
    private String material;
    private double weight;


    private List<Integer> sizes;
    private List<String> colors;
    private List<MultipartFile> images;

}
