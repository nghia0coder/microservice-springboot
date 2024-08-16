package com.ecommercialapp.product.service;

import com.ecommercialapp.product.dto.request.CategoryCreateRequest;
import com.ecommercialapp.product.dto.request.ProductPurchaseRequest;
import com.ecommercialapp.product.dto.request.ProductRequest;
import com.ecommercialapp.product.dto.response.CategoryResponse;
import com.ecommercialapp.product.dto.response.ProductPurchaseResponse;
import com.ecommercialapp.product.dto.response.ProductResponse;
import com.ecommercialapp.product.entity.Category;
import com.ecommercialapp.product.entity.Product;
import com.ecommercialapp.product.exception.AppException;
import com.ecommercialapp.product.exception.ErrorCode;
import com.ecommercialapp.product.mapper.ProductMapper;
import com.ecommercialapp.product.repositories.ProductRepository;
import com.ecommercialapp.product.ultilities.SD;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryService categoryService;
    ProductMapper mapper;

    @Autowired
    SD sd;

    public ProductResponse createProduct(ProductRequest request)  {


        var product = mapper.toProduct(request,categoryService);
        try {
            productRepository.save(product);
        }
        catch (RuntimeException exception)
        {
            throw new RuntimeException(exception.getMessage());
        }


        return mapper.toProductResponse(product);
    }


    public ProductResponse findById(Integer productId) {
        return productRepository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product Not Found with the ID : " + productId));
    }

    public List<ProductResponse> getAll()
    {
        return productRepository.findAll().stream().map(mapper::toProductResponse).toList();
    }

    public List<ProductPurchaseResponse> purchaseProducts(
            List<ProductPurchaseRequest> request
    )
    {
        var productIds = request.stream().map(ProductPurchaseRequest :: productId)
                .toList();
        var storedProducts = productRepository.findAllByIdOrderById(productIds);

        if (productIds.size() != storedProducts.size())
        {
            throw new RuntimeException("One or more Product does not exist");
        }
        var sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new AppException(ErrorCode.QUANTITY_NOT_ENOUGH);
            }
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            productRepository.save(product);
            purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
        }
        return purchasedProducts;
    }

    public ProductResponse updateProduct(Integer productId, ProductRequest request)
    {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION)
        );

        mapper.updateProduct(product,request,categoryService);

        try
        {
            product = productRepository.save(product);
        }
        catch (DuplicateKeyException e)
        {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        return  mapper.toProductResponse(product);
    }

    public void deleteProduct(Integer productId)
    {
        productRepository.deleteById(productId);
    }
}
