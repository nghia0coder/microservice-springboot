package com.ecommerical.order.dto.response;

public record OrderLineResponse (
        Integer id,
        Integer productId,
        double quantity
) {
}
