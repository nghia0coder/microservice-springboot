package com.ecommercial.frontend.service;
import com.ecommercial.frontend.models.response.APIResponse;
import com.ecommercial.frontend.models.ProductDTO;
import com.ecommercial.frontend.models.request.RequestDTO;
import com.ecommercial.frontend.ultilities.SD;
import com.ecommercial.frontend.ultilities.UrlConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductService  {

    BaseService baseService;

    @Autowired
    UrlConfig applicationConfig;

    public APIResponse getAllProduct() {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setAPItype(SD.ApiType.GET);
        requestDTO.setUrl(applicationConfig.getProductUrl());
        requestDTO.setContentType(SD.ContentType.Json);

        return baseService.Send(requestDTO);
    }
    public APIResponse getProductById(Integer productId) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setAPItype(SD.ApiType.GET);
        requestDTO.setUrl(applicationConfig.getProductUrl() + "/" + productId);
        requestDTO.setContentType(SD.ContentType.Json);

        return baseService.Send(requestDTO);
    }
    public APIResponse createProduct(ProductDTO productDTO) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setAPItype(SD.ApiType.POST);
        requestDTO.setUrl(applicationConfig.getProductUrl());
        requestDTO.setData(productDTO);
        requestDTO.setContentType(SD.ContentType.Json);
        return baseService.Send(requestDTO);
    }

    public APIResponse updateProduct(Integer productId, ProductDTO productDTO) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setAPItype(SD.ApiType.PUT);
        requestDTO.setUrl(applicationConfig.getProductUrl() + "/" + productId);
        requestDTO.setData(productDTO);
        requestDTO.setContentType(SD.ContentType.Json);
        return baseService.Send(requestDTO);
    }
    public APIResponse deleteProduct(Integer productId) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setAPItype(SD.ApiType.DELETE);
        requestDTO.setUrl(applicationConfig.getProductUrl() + "/" + productId);
        requestDTO.setContentType(SD.ContentType.Json);
        return baseService.Send(requestDTO);
    }
    public Object deserializeObject(String jsonString)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode resultNode = rootNode.path("result");

            if (resultNode.isArray()) {

                return objectMapper.readValue(resultNode.toString(), new TypeReference<List<ProductDTO>>() {});
            } else if (resultNode.isObject()) {

                return objectMapper.readValue(resultNode.toString(), ProductDTO.class);
            } else {
                // Handle unexpected cases or throw an exception
                throw new IllegalArgumentException("Invalid JSON format: 'result' should be either an array or an object.");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
