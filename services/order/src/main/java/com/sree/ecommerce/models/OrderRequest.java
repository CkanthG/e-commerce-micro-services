package com.sree.ecommerce.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer id,
        String reference,
        @Positive(message = "Order amount should be positive")
        BigDecimal amount,
        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,
        @NotEmpty(message = "Customer should not be null or empty")
        String customerId,
        @NotEmpty(message = "You should purchase at least one product")
        List<PurchaseRequest> products
) {
}
