package com.sree.ecommerce.kafka;

import com.sree.ecommerce.models.CustomerResponse;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.models.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products

) {
}
