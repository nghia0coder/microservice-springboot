package com.example.gateway.service;

import com.example.gateway.client.CustomerClient;
import com.example.gateway.dto.request.AuthenticationRequest;
import com.example.gateway.dto.request.IntrospectRequest;
import com.example.gateway.dto.response.CustomerResponse;
import com.example.gateway.dto.response.AuthenticationResponse;
import com.example.gateway.dto.response.IntrospectResponse;
import com.example.gateway.exception.AppException;
import com.example.gateway.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    
    CustomerClient customerClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var customer = this.customerClient.findCustomerByUserName(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), customer.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(customer);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }


    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        return IntrospectResponse.builder()
                .isValid(verified && expiryTime.after(new Date()))
                .build();
    }



    private String generateToken(CustomerResponse customerDTO) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(customerDTO.getUsername())
                .issuer("nghianghia")

                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(customerDTO))
                .claim("customerId", customerDTO.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(CustomerResponse customerResponse)
    {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(customerResponse.getRoles()))
        {
            customerResponse.getRoles().forEach(stringJoiner::add);
        }

        return stringJoiner.toString();
    }
}
