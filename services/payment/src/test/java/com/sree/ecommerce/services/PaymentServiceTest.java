package com.sree.ecommerce.services;

import com.sree.ecommerce.entities.Payment;
import com.sree.ecommerce.kafka.NotificationProducer;
import com.sree.ecommerce.kafka.PaymentNotificationRequest;
import com.sree.ecommerce.mapper.PaymentMapper;
import com.sree.ecommerce.models.Customer;
import com.sree.ecommerce.models.PaymentMethod;
import com.sree.ecommerce.models.PaymentRequest;
import com.sree.ecommerce.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService service;
    @Mock
    private PaymentRepository repository;
    @Mock
    private PaymentMapper mapper;
    @Mock
    private NotificationProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoPayment_Success() {
        // given
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
        Payment payment = Payment.builder()
                .id(1)
                .orderId(1)
                .paymentMethod(PaymentMethod.PAYPAL)
                .amount(amount)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        PaymentNotificationRequest paymentNotificationRequest = new PaymentNotificationRequest(
                orderReference,
                amount,
                PaymentMethod.PAYPAL,
                firstName,
                lastName,
                email
        );
        // when
        when(mapper.toPayment(paymentRequest)).thenReturn(payment);
        when(repository.save(payment)).thenReturn(payment);
        doNothing().when(producer).sendNotification(paymentNotificationRequest);
        // then
        var actual = service.doPayment(paymentRequest);
        assertNotNull(actual);
        // verify
        verify(mapper, times(1)).toPayment(paymentRequest);
        verify(repository, times(1)).save(payment);
        verify(producer, times(1)).sendNotification(paymentNotificationRequest);
    }
}