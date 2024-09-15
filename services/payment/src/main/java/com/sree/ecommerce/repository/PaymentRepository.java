package com.sree.ecommerce.repository;

import com.sree.ecommerce.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
