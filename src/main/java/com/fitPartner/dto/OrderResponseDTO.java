package com.fitPartner.dto;

import com.fitPartner.entity.order.Orders;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponseDTO {
    private Long id;
    private String userEmail;
    private double totalAmount;
    private String orderStatus;
    private String paymentMethod;
    private String paymentId;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
    private List<OrderItemDTO> items;

    public OrderResponseDTO(Orders order) {
        this.id = order.getId();
        this.userEmail = order.getUserEmail();
        this.totalAmount = order.getTotalAmount();
        this.orderStatus = order.getOrderStatus();
        this.paymentMethod = order.getPaymentMethod();
        this.paymentId = order.getPaymentId();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
        this.items = order.getItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
    }
}
