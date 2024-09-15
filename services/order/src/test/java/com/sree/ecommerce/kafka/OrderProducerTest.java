package com.sree.ecommerce.kafka;

import com.sree.ecommerce.models.CustomerResponse;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.models.PurchaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

class OrderProducerTest {

    @InjectMocks
    OrderProducer producer;
    @Mock
    private KafkaTemplate<String, OrderConfirmation> kafkaTemplate;
    @Mock
    private Environment environment;
    final String customerId = UUID.randomUUID().toString();
    final String reference = "GRDEJDHGH";
    final BigDecimal amount = BigDecimal.valueOf(200.0);
    final String firstName = "sree";
    final String lastName = "kanth";
    final String email = "s@gmail.com";
    final Integer productId = 1;
    final String productName = "product";
    final String productDesc = "product-desc";
    final double quantity = 1;
    private CustomerResponse customerResponse;
    private PurchaseResponse purchaseResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendOrderConfirmation_Success() {
        customerResponse = new CustomerResponse(customerId, firstName, lastName, email);
        purchaseResponse = new PurchaseResponse(
                productId,
                productName,
                productDesc,
                amount,
                quantity
        );
        OrderConfirmation orderConfirmation = new OrderConfirmation(
                reference, amount, PaymentMethod.PAYPAL, customerResponse, List.of(purchaseResponse)
        );
        ArgumentCaptor<GenericMessage> captor = ArgumentCaptor.forClass(GenericMessage.class);
        // when
        when(environment.getProperty("spring.kafka.topic")).thenReturn("order-topic");
        final Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation)
                .setHeader(TOPIC, environment.getProperty("spring.kafka.topic"))
                .build();
        CompletableFuture<SendResult<String, OrderConfirmation>> completableFuture = new CompletableFuture<>();
        when(kafkaTemplate.send(message)).thenReturn(completableFuture);
        // then
        producer.sendOrderConfirmation(orderConfirmation);
        // verify
        verify(environment, times(2)).getProperty("spring.kafka.topic");
        verify(kafkaTemplate, times(1)).send(captor.capture());
        assertEquals(orderConfirmation, captor.getValue().getPayload());
    }
}