package com.e_commerce.demo.controller;

import com.e_commerce.demo.dto.request.PaymentRequest;
import com.e_commerce.demo.dto.response.PaymentResponse;
import com.e_commerce.demo.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse response = paymentService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(paymentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        paymentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}