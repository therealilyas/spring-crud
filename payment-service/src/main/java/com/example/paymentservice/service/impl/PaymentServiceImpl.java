package com.example.paymentservice.service.impl;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.dto.UserResponse;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.exception.ResourceNotFoundException;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;  // ← NEW

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;  // ← NEW

    private static final String USER_SERVICE_URL = "http://localhost:8081/api/users/";  // ← NEW
    private static final String ORDER_SERVICE_URL = "http://localhost:8082/api/orders/";  // ← NEW

    // ← MODIFIED constructor
    public PaymentServiceImpl(PaymentRepository paymentRepository, RestTemplate restTemplate) {
        this.paymentRepository = paymentRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public PaymentResponse create(PaymentRequest request) {
        // 1. ⭐ NEW: Validate User exists
        UserResponse user = restTemplate.getForObject(
                USER_SERVICE_URL + request.getUserId(),
                UserResponse.class
        );
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + request.getUserId());
        }

        // 2. ⭐ NEW: Validate Order exists
        // Note: Order Service doesn't have a DTO for this yet, but we can still call it
        try {
            restTemplate.getForObject(
                    ORDER_SERVICE_URL + request.getOrderId(),
                    Object.class
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException("Order not found: " + request.getOrderId());
        }

        // 3. Create Payment
        Payment payment = new Payment();
        apply(payment, request);
        return toResponse(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse getById(Long id) {
        return toResponse(paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + id)));
    }

    @Override
    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PaymentResponse update(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + id));
        apply(payment, request);
        return toResponse(paymentRepository.save(payment));
    }

    @Override
    public void delete(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    private void apply(Payment payment, PaymentRequest request) {
        payment.setUserId(request.getUserId());  // ← NEW
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(request.getStatus());
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getUserId(),  // ← NEW
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus()
        );
    }
}