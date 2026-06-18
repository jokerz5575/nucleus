package hu.clinvio.ui.security.jwt;

import hu.clinvio.ui.security.config.SecurityProperties;
import hu.clinvio.ui.security.service.CvJwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvJwtServiceTest {

    private CvJwtService service;

    @BeforeEach
    void setUp() {
        SecurityProperties props = new SecurityProperties();
        props.getJwt().setSecret("test-secret-key-that-is-at-least-32-characters-long!!");
        props.getJwt().setExpiration(3600000);
        service = new CvJwtService(props);
    }

    @Test
    void generateToken_shouldProduceNonNullToken() {
        String token = service.generateToken("user123", "testuser", "USER");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void validateToken_shouldSucceedForValidToken() {
        String token = service.generateToken("user123", "testuser", "USER");
        assertTrue(service.validateToken(token));
    }

    @Test
    void validateToken_shouldFailForExpiredToken() {
        SecurityProperties props = new SecurityProperties();
        props.getJwt().setSecret("test-secret-key-that-is-at-least-32-characters-long!!");
        props.getJwt().setExpiration(-3600000);
        CvJwtService expiredService = new CvJwtService(props);

        String token = expiredService.generateToken("user123", "testuser", "USER");
        assertFalse(expiredService.validateToken(token));
    }

    @Test
    void validateToken_shouldFailForTamperedToken() {
        String token = service.generateToken("user123", "testuser", "USER");
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";
        assertFalse(service.validateToken(tampered));
    }

    @Test
    void getUserId_shouldReturnSubjectClaim() {
        String token = service.generateToken("user123", "testuser", "USER");
        assertEquals("user123", service.getUserId(token));
    }

    @Test
    void constructorWithCustomSecretAndExpiration_shouldWork() {
        SecurityProperties props = new SecurityProperties();
        props.getJwt().setSecret("another-custom-secret-that-is-at-least-32-chars!!");
        props.getJwt().setExpiration(7200000);
        CvJwtService customService = new CvJwtService(props);

        String token = customService.generateToken("u1", "alice", "ADMIN");
        assertNotNull(token);
        assertTrue(customService.validateToken(token));
        assertEquals("u1", customService.getUserId(token));
    }
}
