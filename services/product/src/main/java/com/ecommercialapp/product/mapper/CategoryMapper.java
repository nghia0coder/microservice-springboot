package com.ecommercialapp.product.mapper;

import com.ecommercialapp.product.dto.request.CategoryCreateRequest;
import com.ecommercialapp.product.dto.response.CategoryResponse;
import com.ecommercialapp.product.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "products", ignore = true)

    Category toCategory (CategoryCreateRequest request);

    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateCategory(@MappingTarget Category category, CategoryCreateRequest request);
}
