package com.ecommercial.payment.repository;

import com.ecommercial.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
}
