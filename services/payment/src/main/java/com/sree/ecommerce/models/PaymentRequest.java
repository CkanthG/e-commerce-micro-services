package com.sree.ecommerce.models;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        @NotNull(message = "Amount is mandatory")
        BigDecimal amount,
        @NotNull(message = "Payment method is mandatory")
        PaymentMethod paymentMethod,
        @NotNull(message = "Order id is mandatory")
        Integer orderId,
        @NotNull(message = "Order reference is mandatory")
        String orderReference,
        Customer customer
) {
}
