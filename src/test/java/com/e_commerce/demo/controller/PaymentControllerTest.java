package com.e_commerce.demo.controller;

import com.e_commerce.demo.dto.request.PaymentRequest;
import com.e_commerce.demo.dto.response.PaymentResponse;
import com.e_commerce.demo.exception.ResourceNotFoundException;
import com.e_commerce.demo.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    private PaymentResponse buildResponse() {
        PaymentResponse response = new PaymentResponse();
        response.setId(5L);
        response.setAmount(999.99);
        response.setMethod("CARD");
        response.setOrderId(10L);
        return response;
    }

    private PaymentRequest buildRequest() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(999.99);
        request.setMethod("CARD");
        request.setOrderId(10L);
        return request;
    }

    @Test
    void create_returnsCreatedResponse() throws Exception {
        when(paymentService.save(any(PaymentRequest.class))).thenReturn(buildResponse());

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.method").value("CARD"))
                .andExpect(jsonPath("$.orderId").value(10L));
    }

    @Test
    void create_withInvalidRequest_returnsBadRequest() throws Exception {
        PaymentRequest invalid = new PaymentRequest();
        invalid.setAmount(-1.0);
        invalid.setMethod("");

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verify(paymentService, never()).save(any());
    }

    @Test
    void getAll_returnsListOfPayments() throws Exception {
        when(paymentService.getAll()).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_whenFound_returnsPayment() throws Exception {
        when(paymentService.getById(5L)).thenReturn(buildResponse());

        mockMvc.perform(get("/payments/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    void getById_whenNotFound_returnsNotFound() throws Exception {
        when(paymentService.getById(999L)).thenThrow(new ResourceNotFoundException("Payment not found with id: 999"));

        mockMvc.perform(get("/payments/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_returnsUpdatedPayment() throws Exception {
        when(paymentService.update(eq(5L), any(PaymentRequest.class))).thenReturn(buildResponse());

        mockMvc.perform(put("/payments/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.method").value("CARD"));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        doNothing().when(paymentService).delete(5L);

        mockMvc.perform(delete("/payments/{id}", 5L))
                .andExpect(status().isNoContent());

        verify(paymentService).delete(5L);
    }
}
