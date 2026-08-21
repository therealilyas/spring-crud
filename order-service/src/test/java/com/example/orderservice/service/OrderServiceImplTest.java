package com.example.orderservice.service;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UserResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserClient userClient;  // ← ADD THIS MOCK

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(
                orderRepository,
                userClient,      // ← ADD THIS
                restTemplate
        );
    }

    private OrderRequest request() {
        OrderRequest request = new OrderRequest();
        request.setUserId(1L);
        request.setProductName("Laptop");
        request.setQuantity(1);
        request.setTotalAmount(new BigDecimal("1200.00"));
        request.setStatus(OrderStatus.CREATED);
        return request;
    }

    @Test
    void create_shouldValidateUserAndSaveOrder() {
        UserResponse user = new UserResponse();

        Order saved = new Order(1L, 1L, "Laptop", 1,
                new BigDecimal("1200.00"), OrderStatus.CREATED);

        // Mock UserClient instead of RestTemplate directly
        doNothing().when(userClient).getUser(1L);
        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        OrderResponse response = orderService.create(request());

        assertEquals(1L, response.getId());
        verify(userClient).getUser(1L);  // ← Changed to UserClient
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void create_shouldFailWhenUserServiceFails() {
        // Mock UserClient to throw exception
        doThrow(new RestClientException("connection failed"))
                .when(userClient).getUser(1L);

        assertThrows(IllegalArgumentException.class,
                () -> orderService.create(request()));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getById_shouldReturnOrder() {
        Order order = new Order(1L, 1L, "Laptop", 1,
                new BigDecimal("1200.00"), OrderStatus.CREATED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertEquals("Laptop", orderService.getById(1L).getProductName());
    }

    @Test
    void getById_shouldThrowWhenMissing() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.getById(99L));
    }

    @Test
    void getAll_shouldReturnOrders() {
        Order order = new Order(1L, 1L, "Laptop", 1,
                new BigDecimal("1200.00"), OrderStatus.CREATED);

        when(orderRepository.findAll()).thenReturn(List.of(order));

        assertEquals(1, orderService.getAll().size());
    }

    @Test
    void update_shouldValidateUserAndSaveOrder() {
        Order existing = new Order(1L, 1L, "Old", 1,
                new BigDecimal("500.00"), OrderStatus.CREATED);

        doNothing().when(userClient).getUser(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(orderRepository.save(any(Order.class))).thenReturn(existing);

        OrderRequest request = request();
        request.setProductName("New");

        orderService.update(1L, request);

        assertEquals("New", existing.getProductName());
        verify(orderRepository).save(existing);
    }

    @Test
    void delete_shouldDeleteExistingOrder() {
        when(orderRepository.existsById(1L)).thenReturn(true);

        orderService.delete(1L);

        verify(orderRepository).deleteById(1L);
    }
}