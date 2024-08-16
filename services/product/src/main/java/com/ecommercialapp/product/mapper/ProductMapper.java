package com.ecommercialapp.product.mapper;

import com.ecommercialapp.product.dto.request.CategoryCreateRequest;
import com.ecommercialapp.product.dto.request.ProductRequest;
import com.ecommercialapp.product.dto.response.CategoryResponse;
import com.ecommercialapp.product.dto.response.ProductPurchaseResponse;
import com.ecommercialapp.product.dto.response.ProductResponse;
import com.ecommercialapp.product.entity.Category;
import com.ecommercialapp.product.entity.Product;
import com.ecommercialapp.product.service.CategoryService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "categoryId", target = "category")
    Product toProduct(ProductRequest request, @Context CategoryService categoryService);

    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "categoryName", source = "product.category.name")
    ProductResponse toProductResponse(Product product);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(target = "id", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductRequest request, @Context CategoryService categoryService);

    
    default ProductPurchaseResponse toProductPurchaseResponse(Product product, double quantity) {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }

    default Category mapCategory(Integer categoryId, @Context CategoryService categoryService) {
        return categoryService.findCategoryById(categoryId);
    }



}
