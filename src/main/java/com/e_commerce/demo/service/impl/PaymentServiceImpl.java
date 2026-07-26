package com.e_commerce.demo.service.impl;

import com.e_commerce.demo.dto.request.PaymentRequest;
import com.e_commerce.demo.dto.response.PaymentResponse;
import com.e_commerce.demo.entity.Order;
import com.e_commerce.demo.entity.Payment;
import com.e_commerce.demo.exception.ResourceNotFoundException;
import com.e_commerce.demo.mapper.PaymentMapper;
import com.e_commerce.demo.repository.OrderRepository;
import com.e_commerce.demo.repository.PaymentRepository;
import com.e_commerce.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getById(Long id) {
        return paymentMapper.toResponse(findPaymentOrThrow(id));
    }

    @Override
    public PaymentResponse save(PaymentRequest request) {
        Order order = findOrderOrThrow(request.getOrderId());

        Payment payment = paymentMapper.toEntity(request);
        payment.setOrder(order);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    public PaymentResponse update(Long id, PaymentRequest request) {
        Payment existing = findPaymentOrThrow(id);
        Order order = findOrderOrThrow(request.getOrderId());

        paymentMapper.updateEntityFromRequest(request, existing);
        existing.setOrder(order);

        Payment updatedPayment = paymentRepository.save(existing);
        return paymentMapper.toResponse(updatedPayment);
    }

    @Override
    public void delete(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    private Payment findPaymentOrThrow(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    private Order findOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }
}
