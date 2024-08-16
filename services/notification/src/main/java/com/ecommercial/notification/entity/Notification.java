package com.ecommercial.notification.entity;

import com.ecommercial.notification.kafka.order.OrderConfirmation;
import com.ecommercial.notification.kafka.payment.PaymentConfirmation;
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
    String id;
    NotificationType type;
    LocalDateTime notificationDate;
    OrderConfirmation orderConfirmation;
    PaymentConfirmation paymentConfirmation;
}
