package com.example.orderservice.dto;

import com.example.orderservice.entity.OrderStatus;

import java.math.BigDecimal;

public class OrderResponse {

    private Long id;
    private Long userId;
    private String productName;
    private Integer quantity;
    private BigDecimal totalAmount;
    private OrderStatus status;

    public OrderResponse(Long id, Long userId, String productName, Integer quantity,
                         BigDecimal totalAmount, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
}
