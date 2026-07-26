package com.e_commerce.demo.controller;

import com.e_commerce.demo.dto.request.OrderRequest;
import com.e_commerce.demo.dto.response.OrderResponse;
import com.e_commerce.demo.exception.ResourceNotFoundException;
import com.e_commerce.demo.service.OrderService;
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

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private OrderResponse buildResponse() {
        OrderResponse response = new OrderResponse();
        response.setId(10L);
        response.setProduct("Laptop");
        response.setPrice(999.99);
        response.setUserId(1L);
        return response;
    }

    private OrderRequest buildRequest() {
        OrderRequest request = new OrderRequest();
        request.setProduct("Laptop");
        request.setPrice(999.99);
        request.setUserId(1L);
        return request;
    }

    @Test
    void save_returnsCreatedResponse() throws Exception {
        when(orderService.save(any(OrderRequest.class))).thenReturn(buildResponse());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.product").value("Laptop"))
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    void save_withInvalidRequest_returnsBadRequest() throws Exception {
        OrderRequest invalid = new OrderRequest();
        invalid.setProduct("");
        invalid.setPrice(-5.0);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).save(any());
    }

    @Test
    void getAll_returnsListOfOrders() throws Exception {
        when(orderService.getAll()).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_whenFound_returnsOrder() throws Exception {
        when(orderService.getById(10L)).thenReturn(buildResponse());

        mockMvc.perform(get("/orders/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void getById_whenNotFound_returnsNotFound() throws Exception {
        when(orderService.getById(999L)).thenThrow(new ResourceNotFoundException("Order not found with id: 999"));

        mockMvc.perform(get("/orders/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_returnsUpdatedOrder() throws Exception {
        when(orderService.update(eq(10L), any(OrderRequest.class))).thenReturn(buildResponse());

        mockMvc.perform(put("/orders/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("Laptop"));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        doNothing().when(orderService).delete(10L);

        mockMvc.perform(delete("/orders/{id}", 10L))
                .andExpect(status().isNoContent());

        verify(orderService).delete(10L);
    }
}
