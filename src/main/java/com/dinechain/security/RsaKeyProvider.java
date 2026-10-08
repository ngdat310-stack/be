package com.dinechain.security;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class RsaKeyProvider {

    @Value("${jwt.private-key-path:private_key.pem}")
    private String privateKeyPath;

    @Value("${jwt.public-key-path:public_key.pem}")
    private String publicKeyPath;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    // Load once at startup: lazy loading from many request threads at once raced and caused random 401s under load
    @PostConstruct
    void loadKeys() {
        privateKey = loadPrivateKey();
        publicKey = loadPublicKey();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private PrivateKey loadPrivateKey() {
        try {
            String key = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
            String privateKeyPem = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

            byte[] decodedKey = Base64.getDecoder().decode(privateKeyPem);
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            log.info("RSA private key loaded successfully from: {}", privateKeyPath);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception ex) {
            log.error("Failed to load private key from {}: {}", privateKeyPath, ex.getMessage());
            throw new RuntimeException("Cannot load private key", ex);
        }
    }

    private PublicKey loadPublicKey() {
        try {
            String key = new String(Files.readAllBytes(Paths.get(publicKeyPath)));
            String publicKeyPem = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

            byte[] decodedKey = Base64.getDecoder().decode(publicKeyPem);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            log.info("RSA public key loaded successfully from: {}", publicKeyPath);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            log.error("Failed to load public key from {}: {}", publicKeyPath, ex.getMessage());
            throw new RuntimeException("Cannot load public key", ex);
        }
    }
}
