package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentMethod;
import com.example.paymentservice.entity.PaymentStatus;
import com.example.paymentservice.exception.ResourceNotFoundException;
import com.example.paymentservice.repository.PaymentRepository;
import com.example.paymentservice.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void shouldCreatePaymentSuccessfully() {
        // Given
        PaymentRequest request = new PaymentRequest(
                1L,  // userId
                1L,  // orderId
                new BigDecimal("100.00"),
                PaymentMethod.CARD,
                PaymentStatus.PENDING
        );

        // Mock RestTemplate calls
        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(new com.example.paymentservice.dto.UserResponse(1L, "Ilyas", "ilyas@example.com"));

        Payment payment = new Payment(
                1L,   // id
                1L,   // userId ← NEW
                1L,   // orderId ← NEW
                new BigDecimal("100.00"),
                PaymentMethod.CARD,
                PaymentStatus.PENDING
        );

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // When
        PaymentResponse response = paymentService.create(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(1L, response.getOrderId());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        PaymentRequest request = new PaymentRequest(
                999L,  // userId - doesn't exist
                1L,
                new BigDecimal("100.00"),
                PaymentMethod.CARD,
                PaymentStatus.PENDING
        );

        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            paymentService.create(request);
        });
    }

    // Add other tests as needed
}