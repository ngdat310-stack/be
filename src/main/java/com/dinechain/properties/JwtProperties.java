package com.dinechain.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secretKey; // Fallback for HMAC (deprecated, use RSA)
    private String privateKeyPath = "private_key.pem";
    private String publicKeyPath = "public_key.pem";
    private Long accessTokenExpiration; // seconds
    private Long refreshTokenExpiration; // seconds
}
