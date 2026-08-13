package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse create(PaymentRequest request);
    PaymentResponse getById(Long id);
    List<PaymentResponse> getAll();
    PaymentResponse update(Long id, PaymentRequest request);
    void delete(Long id);
}
