package com.sree.ecommerce.models;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email
) {

}
