package com.ecommerical.order.dto.request;

import com.ecommerical.order.dto.response.CustomerResponse;
import com.ecommerical.order.entity.Customer;
import com.ecommerical.order.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest (

        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        CustomerResponse customer
){
}
