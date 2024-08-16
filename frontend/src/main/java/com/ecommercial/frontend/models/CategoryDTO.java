package com.ecommercial.frontend.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CategoryDTO {
    Integer id;
    @NotEmpty(message = "Category name is required")
    String name;
}
