package com.ecommercialapp.product.controller;


import com.ecommercialapp.product.dto.request.CategoryCreateRequest;
import com.ecommercialapp.product.dto.response.APIResponse;
import com.ecommercialapp.product.dto.response.CategoryResponse;
import com.ecommercialapp.product.entity.Category;
import com.ecommercialapp.product.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/category")
@RestController
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryAPIController {
    @Autowired
    CategoryService categoryService;

    @PostMapping
    APIResponse<CategoryResponse> createCategory(
            @RequestBody @Valid CategoryCreateRequest request
    ) {
        log.warn("Something weird");
        return APIResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @GetMapping
    APIResponse<List<CategoryResponse>> getAllCategories()
    {
        return APIResponse.<List<CategoryResponse>> builder()
                .result(categoryService.getAllCategory())
                .build();
    }


    @GetMapping("/{categoryId}")
    APIResponse<CategoryResponse> getCategoryById(@PathVariable Integer categoryId)
    {
        return APIResponse.<CategoryResponse> builder()
                .result(categoryService.findCategoryId(categoryId))
                .build();
    }

    @DeleteMapping("/{categoryId}")
    APIResponse<String> deleteCategory(@PathVariable Integer categoryId)
    {
        categoryService.deleteCategory(categoryId);
        return  APIResponse.<String>builder()
                .result("Category Has Been Deleted")
                .build();
    }
    @PutMapping("/{categoryId}")
    APIResponse<CategoryResponse> updateCategory(@PathVariable Integer categoryId, @RequestBody CategoryCreateRequest request)
    {
        return  APIResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(categoryId,request))
                .build();
    }
}
