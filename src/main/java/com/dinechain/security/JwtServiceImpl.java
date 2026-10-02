package com.dinechain.security;

import com.dinechain.entity.StaffEntity;
import com.dinechain.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    @Override
    public String generateAccessToken(StaffEntity staff) {
        return Jwts.builder()
            .subject(String.valueOf(staff.getId()))
            .claim("staffId", staff.getId())
            .claim("username", staff.getUsername())
            .claim("role", staff.getRole().name())
            .claim("tokenType", "ACCESS")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration() * 1000))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    public String generateRefreshToken(StaffEntity staff) {
        return Jwts.builder()
            .subject(String.valueOf(staff.getId()))
            .claim("staffId", staff.getId())
            .claim("tokenType", "REFRESH")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration() * 1000))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    public Integer validateAccessToken(String token) {
        try {
            var parser = Jwts.parser().verifyWith(getSigningKey()).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();

            String tokenType = (String) claims.get("tokenType");
            if (!"ACCESS".equals(tokenType)) {
                log.warn("Invalid token type: {}", tokenType);
                return null;
            }

            return ((Number) claims.get("staffId")).intValue();
        } catch (Exception ex) {
            log.debug("Invalid or expired access token: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public Integer validateRefreshToken(String token) {
        try {
            var parser = Jwts.parser().verifyWith(getSigningKey()).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();

            String tokenType = (String) claims.get("tokenType");
            if (!"REFRESH".equals(tokenType)) {
                log.warn("Invalid token type: {}", tokenType);
                return null;
            }

            return ((Number) claims.get("staffId")).intValue();
        } catch (Exception ex) {
            log.debug("Invalid or expired refresh token: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public Integer extractStaffId(String token) {
        try {
            var parser = Jwts.parser().verifyWith(getSigningKey()).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();

            return ((Number) claims.get("staffId")).intValue();
        } catch (Exception ex) {
            log.debug("Failed to extract staffId from token: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            var parser = Jwts.parser().verifyWith(getSigningKey()).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();

            return claims.getExpiration().before(new Date());
        } catch (Exception ex) {
            return true;
        }
    }

    @Override
    public long getAccessTokenExpirationSeconds() {
        return jwtProperties.getAccessTokenExpiration();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }
}
