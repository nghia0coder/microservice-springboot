package com.ecommercial.notification.repositories;

import com.ecommercial.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
