package com.e_commerce.demo.service;

import com.e_commerce.demo.dto.request.PaymentRequest;
import com.e_commerce.demo.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    List<PaymentResponse> getAll();

    PaymentResponse getById(Long id);

    PaymentResponse save(PaymentRequest request);

    PaymentResponse update(Long id, PaymentRequest request);

    void delete(Long id);
}
