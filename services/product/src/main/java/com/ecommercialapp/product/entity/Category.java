package com.ecommercialapp.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @GeneratedValue
    Integer id;
    @NotNull(message = "Category Name is Required")
    String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Product> products;
}
