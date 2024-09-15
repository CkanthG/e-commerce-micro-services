package com.sree.ecommerce.models;

import com.sree.ecommerce.customer.Address;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
) {
}
