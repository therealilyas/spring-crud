package com.e_commerce.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be positive")
    private Double amount;

    @NotBlank(message = "method is required")
    private String method;

    @NotNull(message = "orderId is required")
    private Long orderId;

}
