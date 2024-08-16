package com.ecommerical.order.mapper;

import com.ecommerical.order.dto.request.OrderLineRequest;
import com.ecommerical.order.dto.response.OrderLineResponse;
import com.ecommerical.order.entity.Order;
import com.ecommerical.order.entity.OrderLine;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request, Order order) {
        return OrderLine.builder()
                .productId(request.productId())
                .order(order)
                .quantity(request.quantity())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getProductId(),
                orderLine.getQuantity()
        );
    }
}
