package com.sree.ecommerce.mappers;

import com.sree.ecommerce.entities.Order;
import com.sree.ecommerce.entities.OrderLine;
import com.sree.ecommerce.models.OrderLineRequest;
import com.sree.ecommerce.models.OrderLineResponse;
import com.sree.ecommerce.models.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderLineMapperTest {

    private OrderLineMapper mapper;
    private OrderLineRequest orderLineRequest;
    private OrderLine orderLine;
    private Order order;
    private OrderLineResponse orderLineResponse;
    private final Integer orderId = 1;
    private final Integer productId = 1;
    private final double quantity = 1;
    private final Integer orderLineId = 1;
    final String customerId = UUID.randomUUID().toString();
    final String reference = "GRDEJDHGH";
    final BigDecimal amount = BigDecimal.valueOf(200.0);

    @BeforeEach
    void setUp() {
        mapper = new OrderLineMapper();
        orderLineRequest = new OrderLineRequest(orderLineId, orderId, productId, quantity);
        order = Order.builder()
                .id(orderId)
                .customerId(customerId)
                .totalAmount(amount)
                .paymentMethod(PaymentMethod.PAYPAL)
                .reference(reference)
                .createdDate(LocalDateTime.now())
                .build();
        orderLine = OrderLine.builder()
                .id(orderLineId)
                .productId(productId)
                .quantity(quantity)
                .order(order)
                .build();
        orderLineResponse = new OrderLineResponse(orderLineId, quantity);
    }

    @Test
    void testToOrderLine_Success() {
        var actual = mapper.toOrderLine(orderLineRequest);
        assertEquals(orderLineRequest.id(), actual.getId());
        assertEquals(orderLineRequest.orderId(), actual.getOrder().getId());
        assertEquals(orderLineRequest.productId(), actual.getProductId());
        assertEquals(orderLineRequest.quantity(), actual.getQuantity());
    }

    @Test
    void testToOrderLineResponse_Success() {
        var actual = mapper.toOrderLineResponse(orderLine);
        assertEquals(orderLine.getId(), actual.id());
        assertEquals(orderLine.getQuantity(), actual.quantity());
    }
}