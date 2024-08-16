package com.ecommercial.payment.dto.request;

import com.ecommercial.payment.entity.Customer;
import com.ecommercial.payment.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        Customer customer
) {
}
