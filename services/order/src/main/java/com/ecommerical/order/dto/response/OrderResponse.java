package com.ecommerical.order.dto.response;

import com.ecommerical.order.entity.OrderLine;
import com.ecommerical.order.entity.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record OrderResponse (
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerId,
        List<OrderLineResponse> orderLines
)  {
}
