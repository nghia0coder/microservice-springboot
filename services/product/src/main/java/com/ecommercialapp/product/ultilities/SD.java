package com.ecommercialapp.product.ultilities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "product.image")
@Getter
@Setter
public class SD {
    private String uploadDir;
}
