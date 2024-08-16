package com.ecommercial.frontend.models;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Builder
public class CartItem {
    private Integer productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
}
