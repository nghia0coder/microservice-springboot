package com.ecommerical.order.dto.request;

public record OrderLineRequest(

        Integer orderId,
        Integer productId,
        double quantity
) {
}
