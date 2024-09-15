package com.sree.ecommerce.mapper;

import com.sree.ecommerce.entities.Payment;
import com.sree.ecommerce.models.PaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public Payment toPayment(PaymentRequest request) {
        return Payment.builder()
                .id(request.id())
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .orderId(request.orderId())
                .build();
    }
}
