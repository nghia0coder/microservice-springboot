package com.ecommercialapp.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue
    Integer id;
    String name;
    String description;
    double availableQuantity;
    BigDecimal price;
    String productImageUrl;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
