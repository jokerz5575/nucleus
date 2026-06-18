package hu.clinvio.ui.persistence.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptedStringConverterTest {

    private EncryptedStringConverter converter;
    private AesCryptoService cryptoService;

    @BeforeEach
    void setUp() {
        cryptoService = new AesCryptoService("test-key-for-converter");
        converter = new EncryptedStringConverter();
        converter.setCryptoService(cryptoService);
    }

    @Test
    void convertToDatabaseColumnEncryptsTheValue() {
        String plaintext = "sensitive data";
        String encrypted = converter.convertToDatabaseColumn(plaintext);
        assertNotNull(encrypted);
        assertNotEquals(plaintext, encrypted);
        assertEquals(plaintext, cryptoService.decrypt(encrypted));
    }

    @Test
    void convertToEntityAttributeDecryptsTheValue() {
        String plaintext = "my secret";
        String encrypted = cryptoService.encrypt(plaintext);
        String decrypted = converter.convertToEntityAttribute(encrypted);
        assertEquals(plaintext, decrypted);
    }

    @Test
    void nullHandling() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }
}
