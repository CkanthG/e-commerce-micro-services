package com.sree.ecommerce.models;

public record OrderLineRequest(
        Integer id,
        Integer orderId,
        Integer productId,
        double quantity
) {
}
