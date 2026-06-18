package hu.clinvio.ui.security.service;

import hu.clinvio.ui.security.config.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * Service for JWT token generation and validation.
 */
@Service
public class CvJwtService {

    private static final Logger log = LoggerFactory.getLogger(CvJwtService.class);

    private final SecurityProperties properties;
    private final SecretKey key;

    public CvJwtService(SecurityProperties properties) {
        this.properties = properties;
        String secret = properties.getJwt().getSecret();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret must be configured with clinvio.security.jwt.secret.");
        }
        if (secret.length() < SecurityProperties.MIN_JWT_SECRET_LENGTH) {
            throw new IllegalStateException("JWT secret must be at least 32 characters.");
        }
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate a JWT token.
     */
    public String generateToken(String userId, String username, String... roles) {
        return buildToken(userId, username, null, properties.getJwt().getExpiration(), roles);
    }

    /**
     * Generate a JWT token with custom claims.
     */
    public String generateToken(String userId, String username, Map<String, Object> claims) {
        return buildToken(userId, username, claims, properties.getJwt().getExpiration());
    }

    /**
     * Generate a refresh token with extended expiry.
     */
    public String generateRefreshToken(String userId, String username) {
        return buildToken(userId, username, null, properties.getJwt().getRefreshExpiration());
    }

    /**
     * Validate a refresh token (does not check blacklist).
     */
    public boolean validateRefreshToken(String token) {
        return validateToken(token);
    }

    private String buildToken(String userId, String username, Map<String, Object> claims,
                               long expirationMs, String... roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        JwtBuilder builder = Jwts.builder()
                .subject(userId)
                .claim("username", username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key);

        if (roles != null && roles.length > 0) {
            builder.claim("roles", roles);
        }

        if (claims != null) {
            claims.forEach(builder::claim);
        }

        return builder.compact();
    }

    /**
     * Validate a JWT token.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired");
            return false;
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get the user ID from a token.
     */
    public String getUserId(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Get the username from a token.
     */
    public String getUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    /**
     * Get the roles from a token.
     */
    public String[] getRoles(String token) {
        return getClaims(token).get("roles", String[].class);
    }

    /**
     * Get all claims from a token.
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extract token from Authorization header.
     */
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(properties.getJwt().getPrefix())) {
            return authorizationHeader.substring(properties.getJwt().getPrefix().length());
        }
        return null;
    }
}
