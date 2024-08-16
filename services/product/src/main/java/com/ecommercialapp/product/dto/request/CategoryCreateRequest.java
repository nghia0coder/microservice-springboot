package com.ecommercialapp.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CategoryCreateRequest {
    Integer id;
    @NotNull(message = "Category Name Is Required")
    String name;
}
