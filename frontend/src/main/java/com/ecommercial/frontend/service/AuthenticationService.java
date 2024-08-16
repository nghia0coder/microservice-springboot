package com.ecommercial.frontend.service;

import com.ecommercial.frontend.models.request.LoginRequest;
import com.ecommercial.frontend.models.request.RequestDTO;
import com.ecommercial.frontend.models.response.APIResponse;
import com.ecommercial.frontend.models.response.AuthenticationResponse;
import com.ecommercial.frontend.ultilities.SD;
import com.ecommercial.frontend.ultilities.UrlConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {

    BaseService baseService;

    @Autowired
    UrlConfig applicationConfig;

    public APIResponse login(LoginRequest loginRequest)
    {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setAPItype(SD.ApiType.POST);
        requestDTO.setData(loginRequest);
        requestDTO.setUrl(applicationConfig.getAuthenticationUrl());
        requestDTO.setContentType(SD.ContentType.Json);

        return baseService.Send(requestDTO);
    }

    public Object deserializeObject(String jsonString)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode resultNode = rootNode.path("result");

            if (resultNode.isObject()) {

                return objectMapper.readValue(resultNode.toString(), AuthenticationResponse.class);
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
    public boolean isLogin(HttpSession session)
    {
        return session.getAttribute("loggedInUser") != null;
    }
}
