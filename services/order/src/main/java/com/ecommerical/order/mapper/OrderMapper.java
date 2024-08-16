package com.ecommerical.order.mapper;

import com.ecommerical.order.dto.request.OrderRequest;
import com.ecommerical.order.dto.response.OrderLineResponse;
import com.ecommerical.order.dto.response.OrderResponse;
import com.ecommerical.order.entity.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMapper {
    public Order toOrder(OrderRequest request) {
        if (request == null) {
            return null;
        }
        return Order.builder()
                .id(request.id())
                .totalAmount(request.amount())
                .paymentMethod(request.paymentMethod())
                .customerId(request.customerId())
                .build();
    }

    public OrderResponse fromOrder(Order order) {
        List<OrderLineResponse> orderLineResponses = new ArrayList<>();

        if (order.getOrderLines() != null) {
            orderLineResponses = order.getOrderLines().stream()
                    .map(orderLine -> new OrderLineResponse(
                            orderLine.getId(),
                            orderLine.getProductId(),
                            orderLine.getQuantity()
                    ))
                    .collect(Collectors.toList());
        }


        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId(),
                orderLineResponses
        );
    }
}
