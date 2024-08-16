package com.ecommercialapp.product.controller;

import com.ecommercialapp.product.dto.request.CategoryCreateRequest;
import com.ecommercialapp.product.dto.request.ProductPurchaseRequest;
import com.ecommercialapp.product.dto.request.ProductRequest;
import com.ecommercialapp.product.dto.response.APIResponse;
import com.ecommercialapp.product.dto.response.CategoryResponse;
import com.ecommercialapp.product.dto.response.ProductPurchaseResponse;
import com.ecommercialapp.product.dto.response.ProductResponse;
import com.ecommercialapp.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductAPIController {

    ProductService productService;
    @PostMapping
    public APIResponse<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request)  {
        return APIResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @GetMapping("/{product-id}")
    public APIResponse<ProductResponse> findProductById(@PathVariable("product-id") Integer productId){

        return APIResponse.<ProductResponse>builder()
                .result(productService.findById(productId))
                .build();
    }

    @GetMapping
    public APIResponse<List<ProductResponse>> getAllProducts()
    {
        return APIResponse.<List<ProductResponse>>builder()
                .result(productService.getAll())
                .build();
    }

    @DeleteMapping("/{productId}")
    public APIResponse<String> deleteProduct(@PathVariable Integer productId)
    {
        productService.deleteProduct(productId);
        return  APIResponse.<String>builder()
                .result("Product Has Been Deleted")
                .build();
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody List<ProductPurchaseRequest> request
    ) {
        return ResponseEntity.ok(productService.purchaseProducts(request));
    }

    @PutMapping("/{productId}")
    APIResponse<ProductResponse> updateCategory(@PathVariable Integer productId, @RequestBody ProductRequest request)
    {
        return  APIResponse.<ProductResponse>builder()
                .result(productService.updateProduct(productId,request))
                .build();
    }



}
