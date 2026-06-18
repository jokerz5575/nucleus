package hu.clinvio.ui.security.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class CvPasswordEncoderTest {

    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
    }

    @Test
    void encode_shouldCreateEncodedPassword() {
        String raw = "myPassword123";
        String encoded = encoder.encode(raw);
        assertNotNull(encoded);
        assertNotEquals(raw, encoded);
        assertTrue(encoded.startsWith("$2a$") || encoded.startsWith("$2b$") || encoded.startsWith("$2y$"));
    }

    @Test
    void matches_shouldVerifyCorrectPassword() {
        String raw = "myPassword123";
        String encoded = encoder.encode(raw);
        assertTrue(encoder.matches(raw, encoded));
    }

    @Test
    void matches_shouldRejectWrongPassword() {
        String encoded = encoder.encode("correctPassword");
        assertFalse(encoder.matches("wrongPassword", encoded));
    }

    @Test
    void upgradeEncoding_shouldReturnFalseForNewPassword() {
        String encoded = encoder.encode("freshPassword");
        assertFalse(encoder.upgradeEncoding(encoded));
    }
}
