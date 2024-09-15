package com.sree.ecommerce.entities;

import com.sree.ecommerce.kafka.OrderConfirmation;
import com.sree.ecommerce.kafka.PaymentConfirmation;
import com.sree.ecommerce.models.NotificationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {

    @Id
    private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private OrderConfirmation orderConfirmation;
    private PaymentConfirmation paymentConfirmation;
}
