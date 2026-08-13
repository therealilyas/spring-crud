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
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequest request() {
        PaymentRequest r = new PaymentRequest();
        r.setOrderId(10L);
        r.setAmount(new BigDecimal("100.00"));
        r.setMethod(PaymentMethod.CARD);
        r.setStatus(PaymentStatus.PENDING);
        return r;
    }

    @Test
    void create_shouldSavePayment() {
        Payment payment = new Payment(1L, 10L, new BigDecimal("100.00"),
                PaymentMethod.CARD, PaymentStatus.PENDING);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = paymentService.create(request());

        assertEquals(1L, response.getId());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void getById_shouldReturnPayment() {
        Payment payment = new Payment(1L, 10L, new BigDecimal("100.00"),
                PaymentMethod.CARD, PaymentStatus.PAID);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        assertEquals(PaymentStatus.PAID, paymentService.getById(1L).getStatus());
    }

    @Test
    void getById_shouldThrowWhenMissing() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.getById(99L));
    }

    @Test
    void getAll_shouldReturnPayments() {
        Payment payment = new Payment(1L, 10L, new BigDecimal("100.00"),
                PaymentMethod.CARD, PaymentStatus.PENDING);

        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        assertEquals(1, paymentService.getAll().size());
    }

    @Test
    void update_shouldSaveUpdatedPayment() {
        Payment payment = new Payment(1L, 10L, new BigDecimal("100.00"),
                PaymentMethod.CARD, PaymentStatus.PENDING);

        PaymentRequest request = request();
        request.setStatus(PaymentStatus.PAID);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = paymentService.update(1L, request);

        assertEquals(PaymentStatus.PAID, payment.getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void delete_shouldDeleteExistingPayment() {
        when(paymentRepository.existsById(1L)).thenReturn(true);

        paymentService.delete(1L);

        verify(paymentRepository).deleteById(1L);
    }
}
