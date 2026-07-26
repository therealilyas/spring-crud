package com.e_commerce.demo.dto.response;

import lombok.Data;

@Data
public class PaymentResponse {

    private Long id;
    private Double amount;
    private String method;
    private Long orderId;

}