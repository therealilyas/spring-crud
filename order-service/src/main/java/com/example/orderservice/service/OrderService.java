package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    OrderResponse getById(Long id);
    List<OrderResponse> getAll();
    OrderResponse update(Long id, OrderRequest request);
    void delete(Long id);
}
