package com.ecommercial.payment.service;

import com.ecommercial.payment.dto.request.PaymentNotificationRequest;
import com.ecommercial.payment.dto.request.PaymentRequest;
import com.ecommercial.payment.mapper.PaymentMapper;
import com.ecommercial.payment.notification.NotificationProducer;
import com.ecommercial.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest request) {
        var payment = this.repository.save(this.mapper.toPayment(request));

        this.notificationProducer.sendNotifications(
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
}
