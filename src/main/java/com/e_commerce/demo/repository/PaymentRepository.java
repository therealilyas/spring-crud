package com.e_commerce.demo.repository;

import com.e_commerce.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByMethod(String method);

    List<Payment> findByOrderId(Long orderId);
}