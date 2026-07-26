package com.e_commerce.demo.service;

import com.e_commerce.demo.dto.request.OrderRequest;
import com.e_commerce.demo.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getAll();

    OrderResponse getById(Long id);

    OrderResponse save(OrderRequest request);

    OrderResponse update(Long id, OrderRequest request);

    void delete(Long id);
}
