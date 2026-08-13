package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UserResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    public OrderServiceImpl(OrderRepository orderRepository,
                            RestTemplate restTemplate,
                            @Value("${user-service.url}") String userServiceUrl) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    @Override
    public OrderResponse create(OrderRequest request) {
        validateUser(request.getUserId());

        Order order = new Order();
        apply(order, request);

        return toResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse getById(Long id) {
        return toResponse(orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + id)));
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public OrderResponse update(Long id, OrderRequest request) {
        validateUser(request.getUserId());

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + id));

        apply(order, request);

        return toResponse(orderRepository.save(order));
    }

    @Override
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private void validateUser(Long userId) {
        try {
            restTemplate.getForObject(
                    userServiceUrl + "/api/users/" + userId,
                    UserResponse.class
            );
        } catch (RestClientException ex) {
            throw new IllegalArgumentException(
                    "Cannot create/update order because user " + userId + " does not exist");
        }
    }

    private void apply(Order order, OrderRequest request) {
        order.setUserId(request.getUserId());
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(request.getStatus());
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(order.getId(), order.getUserId(),
                order.getProductName(), order.getQuantity(),
                order.getTotalAmount(), order.getStatus());
    }
}
