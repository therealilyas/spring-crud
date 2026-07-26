package com.e_commerce.demo.service;

import com.e_commerce.demo.dto.request.PaymentRequest;
import com.e_commerce.demo.dto.response.PaymentResponse;
import com.e_commerce.demo.entity.Order;
import com.e_commerce.demo.entity.Payment;
import com.e_commerce.demo.exception.ResourceNotFoundException;
import com.e_commerce.demo.mapper.PaymentMapper;
import com.e_commerce.demo.repository.OrderRepository;
import com.e_commerce.demo.repository.PaymentRepository;
import com.e_commerce.demo.service.impl.PaymentServiceImpl;
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
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Payment payment;
    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(10L);
        order.setProduct("Laptop");
        order.setPrice(999.99);

        payment = new Payment();
        payment.setId(5L);
        payment.setAmount(999.99);
        payment.setMethod("CARD");
        payment.setOrder(order);

        paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(999.99);
        paymentRequest.setMethod("CARD");
        paymentRequest.setOrderId(10L);

        paymentResponse = new PaymentResponse();
        paymentResponse.setId(5L);
        paymentResponse.setAmount(999.99);
        paymentResponse.setMethod("CARD");
        paymentResponse.setOrderId(10L);
    }

    @Test
    void getAll_returnsMappedResponses() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        when(paymentMapper.toResponse(payment)).thenReturn(paymentResponse);

        List<PaymentResponse> result = paymentService.getAll();

        assertThat(result).containsExactly(paymentResponse);
    }

    @Test
    void getById_whenFound_returnsResponse() {
        when(paymentRepository.findById(5L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponse(payment)).thenReturn(paymentResponse);

        PaymentResponse result = paymentService.getById(5L);

        assertThat(result).isEqualTo(paymentResponse);
    }

    @Test
    void getById_whenNotFound_throwsResourceNotFoundException() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.getById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void save_whenOrderExists_resolvesOrderAndPersists() {
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(paymentMapper.toEntity(paymentRequest)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toResponse(payment)).thenReturn(paymentResponse);

        PaymentResponse result = paymentService.save(paymentRequest);

        assertThat(result).isEqualTo(paymentResponse);
        assertThat(payment.getOrder()).isEqualTo(order);
        verify(paymentRepository).save(payment);
    }

    @Test
    void save_whenOrderNotFound_throwsResourceNotFoundException() {
        when(orderRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.save(paymentRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void update_whenPaymentAndOrderExist_updatesAndReturnsResponse() {
        when(paymentRepository.findById(5L)).thenReturn(Optional.of(payment));
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toResponse(payment)).thenReturn(paymentResponse);

        PaymentResponse result = paymentService.update(5L, paymentRequest);

        assertThat(result).isEqualTo(paymentResponse);
        verify(paymentMapper).updateEntityFromRequest(paymentRequest, payment);
    }

    @Test
    void update_whenPaymentNotFound_throwsResourceNotFoundException() {
        when(paymentRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.update(5L, paymentRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void delete_whenExists_deletesPayment() {
        when(paymentRepository.existsById(5L)).thenReturn(true);

        paymentService.delete(5L);

        verify(paymentRepository).deleteById(5L);
    }

    @Test
    void delete_whenNotExists_throwsResourceNotFoundException() {
        when(paymentRepository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> paymentService.delete(5L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(paymentRepository, never()).deleteById(anyLong());
    }
}
