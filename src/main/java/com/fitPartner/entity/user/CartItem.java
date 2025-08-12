package com.fitPartner.entity.user;

import com.fitPartner.entity.shoe.Shoe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private double price;
    private LocalDateTime createdAt;
    private int selectedSize;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now ();
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private MyUser user;

    @ManyToOne
    @JoinColumn(name = "shoes_id")
    private Shoe shoe;
}
