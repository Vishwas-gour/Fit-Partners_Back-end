package com.fitPartner.entity.shoe;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Shoe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private int stock;

    @Column(columnDefinition = "TEXT")
    private String description;


    private double salePrice;
    private double costPrice;
    private double discountParentage;
    private double priceAfterDiscount;


    private String category;
    private String gender;
    private String material;
    private double weight;

    private LocalDateTime createdAt;
    private boolean available;


    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


    @OneToMany(mappedBy = "shoe", cascade = CascadeType.ALL)
    private List<Review> reviews;


    @OneToMany(mappedBy = "shoe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "shoe_size",
            joinColumns = @JoinColumn(name = "shoe_id"),
            inverseJoinColumns = @JoinColumn(name = "size_id")
    )
    private List<Size> sizes;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "shoe_color",
            joinColumns = @JoinColumn(name = "shoe_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private List<Color> colors;

    @Override
    public String toString() {
        return "Shoe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", gender='" + gender + '\'' +
                ", material='" + material + '\'' +
                ", available=" + available +
                '}';
    }


}
