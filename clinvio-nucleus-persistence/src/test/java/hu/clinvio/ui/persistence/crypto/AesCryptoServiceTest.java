package hu.clinvio.ui.persistence.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AesCryptoServiceTest {

    private static final String SECRET = "test-secret-key-12345";
    private AesCryptoService cryptoService;

    @BeforeEach
    void setUp() {
        cryptoService = new AesCryptoService(SECRET);
    }

    @Test
    void encryptDecryptRoundtripReturnsOriginalPlaintext() {
        String plaintext = "Hello, World!";
        String encrypted = cryptoService.encrypt(plaintext);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plaintext, decrypted);
    }

    @Test
    void encryptDecryptEmptyStringWorks() {
        String plaintext = "";
        String encrypted = cryptoService.encrypt(plaintext);
        String decrypted = cryptoService.decrypt(encrypted);
        assertEquals(plaintext, decrypted);
    }

    @Test
    void encryptingSameTextTwiceProducesDifferentCiphertext() {
        String plaintext = "same text";
        String encrypted1 = cryptoService.encrypt(plaintext);
        String encrypted2 = cryptoService.encrypt(plaintext);
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    void isEncryptedReturnsTrueForEncryptedValue() {
        String encrypted = cryptoService.encrypt("check me");
        assertTrue(cryptoService.isEncrypted(encrypted));
    }

    @Test
    void isEncryptedReturnsFalseForPlaintext() {
        assertFalse(cryptoService.isEncrypted("not encrypted"));
    }

    @Test
    void decryptTamperedDataThrowsException() {
        String encrypted = cryptoService.encrypt("tamper test");
        String tampered = encrypted.substring(0, encrypted.length() - 4) + "AAAA";
        assertThrows(AesCryptoService.CryptoException.class, () -> cryptoService.decrypt(tampered));
    }

    @Test
    void nullInputHandling() {
        assertNull(cryptoService.encrypt(null));
        assertNull(cryptoService.decrypt(null));
        assertFalse(cryptoService.isEncrypted(null));
    }
}
