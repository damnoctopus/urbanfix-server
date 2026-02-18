package com.urbanfix.config;

import com.urbanfix.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:3600000}")
    private long expirationInMs;

    private Key signingKey;

    @PostConstruct
    public void init() {
        // Simple deterministic key derivation for student/dev use:
        // - Convert the configured secret to bytes
        // - If shorter than 32 bytes, repeat the bytes to fill 32 bytes
        // - If longer than 32 bytes, truncate to 32 bytes
        // This yields a 256-bit key appropriate for HS256 without requiring a long secret.
        byte[] keyBytes = secret == null ? new byte[0] : secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length == 32) {
            signingKey = Keys.hmacShaKeyFor(keyBytes);
        } else if (keyBytes.length < 32 && keyBytes.length > 0) {
            byte[] padded = new byte[32];
            for (int i = 0; i < padded.length; i++) {
                padded[i] = keyBytes[i % keyBytes.length];
            }
            signingKey = Keys.hmacShaKeyFor(padded);
        } else if (keyBytes.length > 32) {
            byte[] truncated = Arrays.copyOf(keyBytes, 32);
            signingKey = Keys.hmacShaKeyFor(truncated);
        } else {
            // secret is empty or null; fall back to a fixed dev key (not for production)
            byte[] dev = new byte[32];
            Arrays.fill(dev, (byte) 0x1A);
            signingKey = Keys.hmacShaKeyFor(dev);
            log.warn("JWT secret is empty; using fixed development key (not secure for production)");
        }
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expirationInMs);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parser().build().parseClaimsJws(token).getBody();
        return claims.get("userId", Long.class);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parser().build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parser().build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            log.debug("JWT validation failed: {}", ex.getMessage());
        }
        return false;
    }

    private JwtParserBuilder parser() {
        return Jwts.parser().setSigningKey(signingKey);
    }
}
