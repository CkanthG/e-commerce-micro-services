package com.sree.ecommerce.kafka;

import com.sree.ecommerce.entities.Notification;
import com.sree.ecommerce.repository.NotificationRepository;
import com.sree.ecommerce.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.sree.ecommerce.models.NotificationType.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final EmailService service;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info(format("Consuming the message from payment kafka topic: %s", paymentConfirmation));
        repository.save(
                Notification.builder()
                        .paymentConfirmation(paymentConfirmation)
                        .type(PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .build()
        );
        service.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                paymentConfirmation.customerFirstname() + " " + paymentConfirmation.customerLastname(),
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );
    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotification(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info(format("Consuming the message from order kafka topic: %s", orderConfirmation));
        repository.save(
                Notification.builder()
                        .orderConfirmation(orderConfirmation)
                        .type(ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .build()
        );
        service.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                orderConfirmation.customer().firstname() + " " + orderConfirmation.customer().lastname(),
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }
}
