package com.sree.ecommerce.mappers;

import com.sree.ecommerce.entities.Order;
import com.sree.ecommerce.models.OrderRequest;
import com.sree.ecommerce.models.OrderResponse;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.models.PurchaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private OrderMapper mapper;
    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private Order order;
    private List<PurchaseRequest> purchaseRequests;
    final String customerId = UUID.randomUUID().toString();
    final String reference = "GRDEJDHGH";
    final BigDecimal amount = BigDecimal.valueOf(200.0);
    final Integer orderId = 1;
    final Integer productId = 1;
    final double quantity = 1;

    @BeforeEach
    void setUp() {
        mapper = new OrderMapper();
        purchaseRequests = List.of(
                new PurchaseRequest(productId, quantity),
                new PurchaseRequest(2, 2)
        );
        orderRequest = new OrderRequest(orderId, reference, amount, PaymentMethod.PAYPAL, customerId, purchaseRequests);
        order = Order.builder()
                .id(orderId)
                .customerId(customerId)
                .totalAmount(amount)
                .paymentMethod(PaymentMethod.PAYPAL)
                .reference(reference)
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    void testToOrder_Success() {
        var actual = mapper.toOrder(orderRequest);
        assertEquals(orderRequest.id(), actual.getId());
        assertEquals(orderRequest.reference(), actual.getReference());
        assertEquals(orderRequest.paymentMethod(), actual.getPaymentMethod());
        assertEquals(orderRequest.customerId(), actual.getCustomerId());
        assertEquals(orderRequest.amount(), actual.getTotalAmount());
    }

    @Test
    void testToOrderResponse_Success() {
        var actual = mapper.toOrderResponse(order);
        assertEquals(order.getId(), actual.id());
        assertEquals(order.getCustomerId(), actual.customerId());
        assertEquals(order.getPaymentMethod(), actual.paymentMethod());
        assertEquals(order.getReference(), actual.reference());
        assertEquals(order.getTotalAmount(), actual.amount());
    }
}