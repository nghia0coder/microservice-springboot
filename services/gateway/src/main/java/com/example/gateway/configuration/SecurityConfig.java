package com.example.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {"/auth/login","/home/index","/api/products/**","/api/customer", "/api/orders"};

    @Value("${jwt.signerKey}")
    private String signerKey;



    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(PUBLIC_ENDPOINTS).permitAll()  // Public routes
                        .anyExchange().authenticated()            // Secure other routes
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtSpec -> jwtSpec.jwtDecoder(reactiveJwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .csrf(ServerHttpSecurity.CsrfSpec::disable);  // Use JWT for authentication

        return http.build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }


    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        String jwkSetUri = signerKey;  // Replace with your JWK Set URI
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }


}
