package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.dto.PaymentResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.client.UserClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final RestTemplate restTemplate;

    private static final String PAYMENT_SERVICE_URL = "http://localhost:8083/api/payments/";

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserClient userClient,
                            RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.restTemplate = restTemplate;
    }

    @Override
    public OrderResponse create(OrderRequest request) {
        validateUser(request.getUserId());

        Order order = new Order();
        apply(order, request);
        Order savedOrder = orderRepository.save(order);

        try {
            PaymentRequest paymentRequest = new PaymentRequest(
                    savedOrder.getId(),
                    savedOrder.getTotalAmount(),
                    "CARD",
                    "PENDING"
            );

            PaymentResponse payment = restTemplate.postForObject(
                    PAYMENT_SERVICE_URL,
                    paymentRequest,
                    PaymentResponse.class
            );

            if (payment != null && "PAID".equals(payment.getStatus())) {
                savedOrder.setStatus(OrderStatus.CONFIRMED);
                orderRepository.save(savedOrder);
            }
        } catch (Exception e) {
            System.err.println("Payment creation failed: " + e.getMessage());
        }

        return toResponse(savedOrder);
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
        userClient.getUser(userId);
    }

    private void apply(Order order, OrderRequest request) {
        order.setUserId(request.getUserId());
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(request.getTotalAmount());

        order.setStatus(request.getStatus());
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getProductName(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getStatus().name()  );

    }
}