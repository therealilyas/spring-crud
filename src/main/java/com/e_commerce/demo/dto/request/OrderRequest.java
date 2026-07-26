package com.e_commerce.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderRequest {

    @NotBlank(message = "product is required")
    private String product;

    @NotNull(message = "price is required")
    @Positive(message = "price must be positive")
    private Double price;

    @NotNull(message = "userId is required")
    private Long userId;

}
