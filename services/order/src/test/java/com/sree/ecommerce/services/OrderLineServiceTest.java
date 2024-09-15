package com.sree.ecommerce.services;

import com.sree.ecommerce.entities.Order;
import com.sree.ecommerce.entities.OrderLine;
import com.sree.ecommerce.mappers.OrderLineMapper;
import com.sree.ecommerce.models.OrderLineRequest;
import com.sree.ecommerce.models.OrderLineResponse;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.repositories.OrderLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderLineServiceTest {

    @InjectMocks
    private OrderLineService service;
    @Mock
    private OrderLineRepository repository;
    @Mock
    private OrderLineMapper mapper;
    private OrderLineRequest orderLineRequest;
    private OrderLine orderLine;
    private OrderLineResponse orderLineResponse;
    private Order order;
    private final Integer orderId = 1;
    private final Integer productId = 1;
    private final double quantity = 1;
    private final Integer orderLineId = 1;
    final String customerId = UUID.randomUUID().toString();
    final String reference = "GRDEJDHGH";
    final BigDecimal amount = BigDecimal.valueOf(200.0);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void testSaveOrder_Success() {
        // when
        when(mapper.toOrderLine(orderLineRequest)).thenReturn(orderLine);
        when(repository.save(orderLine)).thenReturn(orderLine);
        // then
        var actual = service.saveOrder(orderLineRequest);
        assertEquals(orderLineId, actual);
        // verify
        verify(mapper, times(1)).toOrderLine(orderLineRequest);
        verify(repository, times(1)).save(orderLine);
    }

    @Test
    void testFindAllOrderLinesByOrderId_Success() {
        // when
        when(repository.findAllByOrderId(orderId)).thenReturn(List.of(orderLine));
        when(mapper.toOrderLineResponse(orderLine)).thenReturn(orderLineResponse);
        // then
        var actual = service.findAllOrderLinesByOrderId(orderId);
        var actualOrderLine = actual.getFirst();
        assertEquals(orderLineId, actualOrderLine.id());
        assertEquals(quantity, actualOrderLine.quantity());
        // verify
        verify(repository, times(1)).findAllByOrderId(orderId);
        verify(mapper, times(1)).toOrderLineResponse(orderLine);
    }
}