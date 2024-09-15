package com.sree.ecommerce.repository;

import com.sree.ecommerce.entities.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
