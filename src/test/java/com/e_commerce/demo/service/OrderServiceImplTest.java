package com.e_commerce.demo.service;

import com.e_commerce.demo.dto.request.OrderRequest;
import com.e_commerce.demo.dto.response.OrderResponse;
import com.e_commerce.demo.entity.Order;
import com.e_commerce.demo.entity.User;
import com.e_commerce.demo.exception.ResourceNotFoundException;
import com.e_commerce.demo.mapper.OrderMapper;
import com.e_commerce.demo.repository.OrderRepository;
import com.e_commerce.demo.repository.UserRepository;
import com.e_commerce.demo.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Order order;
    private OrderRequest orderRequest;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
        user.setEmail("john@example.com");

        order = new Order();
        order.setId(10L);
        order.setProduct("Laptop");
        order.setPrice(999.99);
        order.setUser(user);

        orderRequest = new OrderRequest();
        orderRequest.setProduct("Laptop");
        orderRequest.setPrice(999.99);
        orderRequest.setUserId(1L);

        orderResponse = new OrderResponse();
        orderResponse.setId(10L);
        orderResponse.setProduct("Laptop");
        orderResponse.setPrice(999.99);
        orderResponse.setUserId(1L);
    }

    @Test
    void getAll_returnsMappedResponses() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        List<OrderResponse> result = orderService.getAll();

        assertThat(result).containsExactly(orderResponse);
    }

    @Test
    void getById_whenFound_returnsResponse() {
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.getById(10L);

        assertThat(result).isEqualTo(orderResponse);
    }

    @Test
    void getById_whenNotFound_throwsResourceNotFoundException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void save_whenUserExists_resolvesUserAndPersists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.save(orderRequest);

        assertThat(result).isEqualTo(orderResponse);
        assertThat(order.getUser()).isEqualTo(user);
        verify(orderRepository).save(order);
    }

    @Test
    void save_whenUserNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.save(orderRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    void update_whenOrderAndUserExist_updatesAndReturnsResponse() {
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.update(10L, orderRequest);

        assertThat(result).isEqualTo(orderResponse);
        verify(orderMapper).updateEntityFromRequest(orderRequest, order);
        assertThat(order.getUser()).isEqualTo(user);
    }

    @Test
    void update_whenOrderNotFound_throwsResourceNotFoundException() {
        when(orderRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.update(10L, orderRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    void delete_whenExists_deletesOrder() {
        when(orderRepository.existsById(10L)).thenReturn(true);

        orderService.delete(10L);

        verify(orderRepository).deleteById(10L);
    }

    @Test
    void delete_whenNotExists_throwsResourceNotFoundException() {
        when(orderRepository.existsById(10L)).thenReturn(false);

        assertThatThrownBy(() -> orderService.delete(10L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(orderRepository, never()).deleteById(anyLong());
    }
}
