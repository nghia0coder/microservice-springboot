package com.ecommercial.frontend.ultilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@ConfigurationProperties(prefix = "application.config")
@Getter
@Setter
public class UrlConfig {
    private String customerUrl;
    private String paymentUrl;
    private String productUrl;
    private String categoryUrl;
    private String orderUrl;
    private String authenticationUrl;
}
