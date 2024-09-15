package com.sree.ecommerce.controllers;

import com.sree.ecommerce.entities.Order;
import com.sree.ecommerce.entities.OrderLine;
import com.sree.ecommerce.models.OrderLineResponse;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.repositories.OrderLineRepository;
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
class OrderLineControllerTest extends AbstractTestContainers{

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    OrderLineRepository repository;
    @Autowired
    OrderRepository orderRepository;
    private String orderLineUrl;
    @LocalServerPort
    private int port;
    private Order order;

    @BeforeEach
    void setUp() {
        orderLineUrl = "http://localhost:" + port + "/api/v1/order-lines";
        final String customerId = UUID.randomUUID().toString();
        final String reference = "GRDEJDHGH";
        final BigDecimal amount = BigDecimal.valueOf(200.0);
        order = orderRepository.save(Order.builder()
                .id(null)
                .customerId(customerId)
                .totalAmount(amount)
                .paymentMethod(PaymentMethod.PAYPAL)
                .reference(reference)
                .createdDate(LocalDateTime.now())
                .build());
        repository.save(
          OrderLine.builder()
                  .id(null)
                  .productId(1)
                  .quantity(10)
                  .order(order)
                  .build()
        );
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    void getOrderLineById() {
        ResponseEntity<OrderLineResponse[]> responseEntity = restTemplate.getForEntity(
                orderLineUrl + "/order/" + order.getId(),
                OrderLineResponse[].class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}