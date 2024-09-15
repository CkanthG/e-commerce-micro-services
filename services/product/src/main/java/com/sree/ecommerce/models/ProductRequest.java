package com.sree.ecommerce.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,
        @NotNull(message = "Product name is required")
        String name,
        @NotNull(message = "Product description is required")
        String description,
        @NotNull(message = "Product availableQuantity is required")
        @Positive(message = "Product availableQuantity must be positive")
        double availableQuantity,
        @NotNull(message = "Product price is required")
        @Positive(message = "Product price must be positive")
        BigDecimal price,
        @NotNull(message = "Product category is required")
        Integer categoryId
) {
}
