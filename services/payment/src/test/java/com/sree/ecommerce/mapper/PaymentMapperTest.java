package com.sree.ecommerce.mapper;

import com.sree.ecommerce.models.Customer;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.models.PaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    private PaymentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PaymentMapper();
    }

    @Test
    void testToPayment_Success() {
        final String orderReference = "HGDHCHGCF";
        final BigDecimal amount = BigDecimal.valueOf(200.0);
        final String firstName = "sree";
        final String lastName = "kanth";
        final String email = "s@gmail.com";
        PaymentRequest paymentRequest = new PaymentRequest(
                1,
                amount,
                PaymentMethod.PAYPAL,
                1,
                orderReference,
                new Customer(
                        UUID.randomUUID().toString(),
                        firstName,
                        lastName,
                        email
                ));
        var actual = mapper.toPayment(paymentRequest);
        assertEquals(paymentRequest.id(), actual.getId());
        assertEquals(paymentRequest.paymentMethod(), actual.getPaymentMethod());
        assertEquals(paymentRequest.orderId(), actual.getOrderId());
        assertEquals(paymentRequest.amount(), actual.getAmount());
    }
}