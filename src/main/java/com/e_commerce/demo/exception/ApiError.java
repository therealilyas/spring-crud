package com.e_commerce.demo.exception;

import java.time.Instant;

public record ApiError(int status, String message, Instant timestamp) {

    public ApiError(int status, String message) {
        this(status, message, Instant.now());
    }
}
