package com.fitPartner.entity.order;

import com.fitPartner.entity.user.MyUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private double totalAmount;
    private String orderStatus; // PLACED, SHIPPED, DELIVERED, CANCELLED, RETURNED, RETURNING
    private String paymentMethod; // CASH, CARD, UPI, CASH_ON_DELIVERY.
    private String paymentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private MyUser assignedTo;

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", totalAmount=" + totalAmount +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
