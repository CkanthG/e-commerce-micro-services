package com.sree.ecommerce.kafka;

import com.sree.ecommerce.models.Customer;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.models.Product;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        Customer customer,
        List<Product> products
) {
}
