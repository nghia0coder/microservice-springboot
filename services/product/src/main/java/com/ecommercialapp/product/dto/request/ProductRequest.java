package com.ecommercialapp.product.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductRequest {
    Integer id;
    @NotNull(message = "Product Name Is Required")
    String name;
    String description;
    @Positive(message = "Available quantity should be positive")
    double availableQuantity;
    @Positive(message = "Price should be positive")
    BigDecimal price;
    @NotNull(message = "Category Is Required")
    Integer categoryId;

    String imageUrl;
    private String imageLocalPath;

    MultipartFile image;
}
