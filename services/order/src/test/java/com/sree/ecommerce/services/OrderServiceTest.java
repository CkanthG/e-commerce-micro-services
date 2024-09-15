package com.sree.ecommerce.services;

import com.sree.ecommerce.entities.Order;
import com.sree.ecommerce.exceptions.BusinessException;
import com.sree.ecommerce.exceptions.OrderNotFoundException;
import com.sree.ecommerce.kafka.OrderConfirmation;
import com.sree.ecommerce.kafka.OrderProducer;
import com.sree.ecommerce.mappers.OrderMapper;
import com.sree.ecommerce.models.*;
import com.sree.ecommerce.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService service;
    @Mock
    private OrderRepository repository;
    @Mock
    private OrderMapper mapper;
    @Mock
    private CustomerFeignClient customerFeignClient;
    @Mock
    private ProductFeignClient productFeignClient;
    @Mock
    private OrderLineService orderLineService;
    @Mock
    private OrderProducer producer;
    @Mock
    private PaymentFeignClient paymentFeignClient;
    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private CustomerResponse customerResponse;
    private PurchaseResponse purchaseResponse;
    private List<PurchaseRequest> purchaseRequests;
    private Order order;
    private OrderLineRequest orderLineRequest;
    private PaymentRequest paymentRequest;
    private OrderConfirmation orderConfirmation;
    final String customerId = UUID.randomUUID().toString();
    final String reference = "GRDEJDHGH";
    final BigDecimal amount = BigDecimal.valueOf(200.0);
    final String firstName = "sree";
    final String lastName = "kanth";
    final String email = "s@gmail.com";
    final Integer orderId = 1;
    final Integer productId = 1;
    final String productName = "product";
    final String productDesc = "product-desc";
    final double quantity = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // given
        customerResponse = new CustomerResponse(customerId, firstName, lastName, email);
        purchaseResponse = new PurchaseResponse(
                productId,
                productName,
                productDesc,
                amount,
                quantity
        );
        purchaseRequests = List.of(
                new PurchaseRequest(productId, quantity),
                new PurchaseRequest(2, 2)
        );
        orderRequest = new OrderRequest(orderId, reference, amount, PaymentMethod.PAYPAL, customerId, purchaseRequests);
        orderResponse = new OrderResponse(orderId, reference, amount, PaymentMethod.PAYPAL, customerId);
        order = Order.builder()
                .id(orderId)
                .customerId(customerId)
                .totalAmount(amount)
                .paymentMethod(PaymentMethod.PAYPAL)
                .reference(reference)
                .createdDate(LocalDateTime.now())
                .build();
        orderLineRequest = new OrderLineRequest(null, orderId, productId, quantity);
        paymentRequest = new PaymentRequest(amount, PaymentMethod.PAYPAL, orderId, reference, customerResponse);
        orderConfirmation = new OrderConfirmation(
                reference, amount, PaymentMethod.PAYPAL, customerResponse, List.of(purchaseResponse)
        );
    }

    @Test
    void testCreateOrder_Success() {
        // when
        when(customerFeignClient.findCustomerById(customerId)).thenReturn(Optional.of(customerResponse));
        when(productFeignClient.purchaseProduct(purchaseRequests)).thenReturn(Optional.of(List.of(purchaseResponse)));
        when(mapper.toOrder(orderRequest)).thenReturn(order);
        when(repository.save(order)).thenReturn(order);
        when(orderLineService.saveOrder(orderLineRequest)).thenReturn(1);
        when(paymentFeignClient.requestOrderPayment(paymentRequest)).thenReturn(1);
        doNothing().when(producer).sendOrderConfirmation(orderConfirmation);
        // then
        var actual = service.createOrder(orderRequest);
        assertEquals("Successfully processed order with ID: " + orderId, actual);
        // verify
        verify(customerFeignClient, times(1)).findCustomerById(customerId);
        verify(productFeignClient, times(1)).purchaseProduct(purchaseRequests);
        verify(mapper, times(1)).toOrder(orderRequest);
        verify(repository, times(1)).save(order);
        verify(orderLineService, times(1)).saveOrder(orderLineRequest);
        verify(paymentFeignClient, times(1)).requestOrderPayment(paymentRequest);
        verify(producer, times(1)).sendOrderConfirmation(orderConfirmation);
    }

    @Test
    void testCreateOrder_ThrowsBusinessException() {
        // when
        when(customerFeignClient.findCustomerById(customerId)).thenThrow(BusinessException.class);
        assertThrows(
                BusinessException.class,
                () -> service.createOrder(orderRequest)
        );
    }

    @Test
    void testFindAllOrders_Success() {
        // when
        when(repository.findAll()).thenReturn(List.of(order));
        when(mapper.toOrderResponse(order)).thenReturn(orderResponse);
        // then
        var actual = service.findAllOrders();
        assertEquals(1, actual.size());
        var actualOrder = actual.getFirst();
        assertEquals(order.getId(), actualOrder.id());
        assertEquals(order.getReference(), actualOrder.reference());
        assertEquals(order.getCustomerId(), actualOrder.customerId());
        assertEquals(order.getTotalAmount(), actualOrder.amount());
        assertEquals(order.getPaymentMethod(), actualOrder.paymentMethod());
    }

    @Test
    void testFindOrderById_Success() {
        // when
        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(mapper.toOrderResponse(order)).thenReturn(orderResponse);
        // then
        var actual = service.findOrderById(orderId);
        assertEquals(order.getId(), actual.id());
        assertEquals(order.getPaymentMethod(), actual.paymentMethod());
        assertEquals(order.getTotalAmount(), actual.amount());
        assertEquals(order.getReference(), actual.reference());
        assertEquals(order.getCustomerId(), actual.customerId());
    }

    @Test
    void testFindOrderById_ThrowsOrderNotFoundException() {
        assertThrows(
                OrderNotFoundException.class,
                () -> service.findOrderById(orderId)
        );
    }
}