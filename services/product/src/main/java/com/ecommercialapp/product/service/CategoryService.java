package com.ecommercialapp.product.service;

import com.ecommercialapp.product.dto.request.CategoryCreateRequest;
import com.ecommercialapp.product.dto.response.CategoryResponse;
import com.ecommercialapp.product.entity.Category;
import com.ecommercialapp.product.exception.AppException;
import com.ecommercialapp.product.exception.ErrorCode;
import com.ecommercialapp.product.mapper.CategoryMapper;
import com.ecommercialapp.product.repositories.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategory()
    {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).toList();
    }

    public CategoryResponse createCategory(CategoryCreateRequest createRequest)
    {
        if (createRequest.getName() == null || createRequest.getName().trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }
        var category = categoryMapper.toCategory(createRequest);

        try {
            categoryRepository.save(category);
        }
        catch (DuplicateKeyException e)
        {
            log.warn(e.getMessage());
            throw new AppException(ErrorCode.CATEGORY_EXISTED) ;
        }
        return categoryMapper.toCategoryResponse(category);
    }

    public CategoryResponse updateCategory(Integer categoryId, CategoryCreateRequest request)
    {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION)
        );

        categoryMapper.updateCategory(category,request);

        try
        {
            categoryRepository.save(category);
        }
        catch (DuplicateKeyException e)
        {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return  categoryMapper.toCategoryResponse(category);
    }

    public void deleteCategory(Integer categoryId)
    {
        categoryRepository.deleteById(categoryId);
    }


    public Category findCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoryResponse findCategoryId(Integer categoryId)
    {
        return categoryMapper.toCategoryResponse(categoryRepository.findById(categoryId).orElseThrow(
                () -> new RuntimeException("Category Not Found")
        ));
    }
}
