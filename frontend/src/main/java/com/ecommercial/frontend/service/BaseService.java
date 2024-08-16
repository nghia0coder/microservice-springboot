package com.ecommercial.frontend.service;


import com.ecommercial.frontend.models.response.APIResponse;
import com.ecommercial.frontend.models.request.RequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Service
public class BaseService {
    @Autowired
    private RestTemplate restTemplate;

    public APIResponse Send(RequestDTO requestDTO) {
        APIResponse responseDTO = new APIResponse();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Object> requestEntity = new HttpEntity<>(requestDTO.getData(), headers);

            ResponseEntity<String> responseEntity;

            switch (requestDTO.getAPItype()) {
                case POST:
                    responseEntity = restTemplate.postForEntity(requestDTO.getUrl(), requestEntity, String.class);
                    break;
                case PUT:
                    restTemplate.put(requestDTO.getUrl(), requestEntity);
                    responseEntity = new ResponseEntity<>(HttpStatus.OK);
                    break;
                case DELETE:
                    restTemplate.delete(requestDTO.getUrl(), requestEntity);
                    responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    break;
                default:
                    responseEntity = restTemplate.exchange(requestDTO.getUrl(), HttpMethod.GET, requestEntity, String.class);
                    break;
            }

            responseDTO.setIsSuccess(responseEntity.getStatusCode().is2xxSuccessful());
            responseDTO.setResult(responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            responseDTO.setIsSuccess(false);
            responseDTO.setMessage(e.getMessage());
        } catch (Exception e) {
            responseDTO.setIsSuccess(false);
            responseDTO.setMessage(e.getMessage());
        }

        return responseDTO;
    }
}
