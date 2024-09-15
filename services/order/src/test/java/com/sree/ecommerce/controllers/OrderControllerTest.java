package com.sree.ecommerce.controllers;

import com.sree.ecommerce.entities.Order;
import com.sree.ecommerce.models.OrderResponse;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.repositories.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OrderControllerTest extends AbstractTestContainers{

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    OrderRepository repository;
    private String orderUrl;
    @LocalServerPort
    private int port;
    private Order order;

    @BeforeEach
    void setUp() {
        orderUrl = "http://localhost:" + port + "/api/v1/orders";
        final String customerId = UUID.randomUUID().toString();
        final String reference = "GRDEJDHGH";
        final BigDecimal amount = BigDecimal.valueOf(200.0);
        order = repository.save(Order.builder()
                        .id(null)
                        .customerId(customerId)
                        .totalAmount(amount)
                        .paymentMethod(PaymentMethod.PAYPAL)
                        .reference(reference)
                        .createdDate(LocalDateTime.now())
                .build());
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void testFindAll_Success() {
        ResponseEntity<OrderResponse[]> responseEntity = restTemplate.getForEntity(
                orderUrl,
                OrderResponse[].class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testFindById_Success() {
        ResponseEntity<OrderResponse> orderResponseResponseEntity = restTemplate.getForEntity(
                orderUrl + "/" + order.getId(),
                OrderResponse.class
        );
        assertEquals(HttpStatus.OK, orderResponseResponseEntity.getStatusCode());
        assert orderResponseResponseEntity.getBody() != null;
        assertEquals(order.getId(), orderResponseResponseEntity.getBody().id());
        assertEquals(order.getPaymentMethod(), orderResponseResponseEntity.getBody().paymentMethod());
        assertEquals(order.getReference(), orderResponseResponseEntity.getBody().reference());
        assertEquals(order.getCustomerId(), orderResponseResponseEntity.getBody().customerId());
        assertEquals(0, order.getTotalAmount().compareTo(orderResponseResponseEntity.getBody().amount()));
    }
}