package com.sree.ecommerce.models;

public record Customer(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
