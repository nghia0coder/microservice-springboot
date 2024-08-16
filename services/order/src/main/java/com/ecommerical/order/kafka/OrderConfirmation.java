package com.ecommerical.order.kafka;

import com.ecommerical.order.dto.response.CustomerResponse;
import com.ecommerical.order.dto.response.PurchaseResponse;
import com.ecommerical.order.entity.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
