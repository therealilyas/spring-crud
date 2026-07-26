package com.e_commerce.demo.service.impl;

import com.e_commerce.demo.dto.request.OrderRequest;
import com.e_commerce.demo.dto.response.OrderResponse;
import com.e_commerce.demo.entity.Order;
import com.e_commerce.demo.entity.User;
import com.e_commerce.demo.exception.ResourceNotFoundException;
import com.e_commerce.demo.mapper.OrderMapper;
import com.e_commerce.demo.repository.OrderRepository;
import com.e_commerce.demo.repository.UserRepository;
import com.e_commerce.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        return orderMapper.toResponse(findOrderOrThrow(id));
    }

    @Override
    public OrderResponse save(OrderRequest request) {
        User user = findUserOrThrow(request.getUserId());

        Order order = orderMapper.toEntity(request);
        order.setUser(user);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public OrderResponse update(Long id, OrderRequest request) {
        Order existing = findOrderOrThrow(id);
        User user = findUserOrThrow(request.getUserId());

        orderMapper.updateEntityFromRequest(request, existing);
        existing.setUser(user);

        Order updatedOrder = orderRepository.save(existing);
        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private Order findOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}
