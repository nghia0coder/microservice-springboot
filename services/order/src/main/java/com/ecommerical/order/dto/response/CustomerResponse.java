package com.ecommerical.order.dto.response;

public record CustomerResponse (
        String id,
        String firstname,
        String lastname,
        String email
) {
}
