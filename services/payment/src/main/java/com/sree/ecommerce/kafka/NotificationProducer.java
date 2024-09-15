package com.sree.ecommerce.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, PaymentNotificationRequest> kafkaTemplate;
    private final Environment environment;

    public void sendNotification(PaymentNotificationRequest request) {
        log.info("Sending notification with body = < {} >", request);
        Message<PaymentNotificationRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(TOPIC, environment.getProperty("spring.kafka.topic"))
                .build();

        kafkaTemplate.send(message);
    }
}
