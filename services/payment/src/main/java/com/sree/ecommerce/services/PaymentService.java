package com.sree.ecommerce.services;

import com.sree.ecommerce.kafka.NotificationProducer;
import com.sree.ecommerce.mapper.PaymentMapper;
import com.sree.ecommerce.kafka.PaymentNotificationRequest;
import com.sree.ecommerce.models.PaymentRequest;
import com.sree.ecommerce.repository.PaymentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer producer;
    private static final String PAYMENT_SERVICE = "payment-service";

    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "paymentFallbackMethod")
    public Integer doPayment(PaymentRequest request) {
        var payment = repository.save(mapper.toPayment(request));
        producer.sendNotification(
                new PaymentNotificationRequest(
                        request.orderReference(),
                        request.amount(),
                        request.paymentMethod(),
                        request.customer().firstname(),
                        request.customer().lastname(),
                        request.customer().email()
                )
        );
        return payment.getId();
    }

    public Integer paymentFallbackMethod(Exception e) {
        log.error("Notification Service is DOWN, please try again after sometime, Exception : {}", e.getLocalizedMessage());
        return 0;
    }
}
