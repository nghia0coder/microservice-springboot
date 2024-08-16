package com.ecommercial.frontend.ultilities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class JwtUtil {

    @Value("${jwt.signerKey}")
    private String signerKey;

    private static String SECRET;

    @PostConstruct
    public void init() {
        SECRET = signerKey;
    }

    public static String getStaticSignerKey() {
        return SECRET;
    }


    public static DecodedJWT decodeJWT(String token) {
        return JWT.require(Algorithm.HMAC512(getStaticSignerKey().getBytes()))
                .build()
                .verify(token);
    }

    public static String getSubFromToken(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        return decodedJWT.getSubject();
    }

    public static String getScopeFromToken(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        return decodedJWT.getClaim("scope").asString();
    }

    public static String getIdFromToken(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        return decodedJWT.getClaim("customerId").asString();
    }
}


