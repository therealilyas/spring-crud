package com.e_commerce.demo.dto.response;

import lombok.Data;

@Data
public class OrderResponse {

    private Long id;
    private String product;
    private Double price;
    private Long userId;

}