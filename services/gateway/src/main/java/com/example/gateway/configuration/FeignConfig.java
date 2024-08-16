package com.example.gateway.configuration;


import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignConfig {
    @Bean
    public HttpMessageConverters messageConverters() {
        HttpMessageConverter<?> converter = new MappingJackson2HttpMessageConverter();
        return new HttpMessageConverters(converter);
    }

}
