package com.ecommercial.frontend.models;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ProductDTO {
        Integer id;
        @NotEmpty(message = "Name must not be null")
        String name;
        String description;
        @NotNull(message = "Quantity must not be null")
        @Positive(message = "Quantity must be greater than 0")
        Integer availableQuantity;
        @NotNull(message = "Price must not be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price;
        @NotNull(message = "Category ID is required")
        @Positive(message = "You must select Category For Product")
        Integer categoryId;
        String categoryName;

        String imageUrl;

        MultipartFile image;

}
