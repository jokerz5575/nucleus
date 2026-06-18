package hu.clinvio.ui.persistence.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-256-GCM encryption service for sensitive data.
 * <p>
 * Provides authenticated encryption with integrity verification.
 * Each encrypted value includes a random IV (nonce) prepended to the ciphertext,
 * ensuring that encrypting the same plaintext twice produces different ciphertext.
 * </p>
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * AesCryptoService crypto = new AesCryptoService("my-secret-key");
 * String encrypted = crypto.encrypt("sensitive data");
 * String decrypted = crypto.decrypt(encrypted);
 * }</pre>
 *
 * <h3>Security properties:</h3>
 * <ul>
 *   <li>AES-256-GCM: authenticated encryption (confidentiality + integrity)</li>
 *   <li>12-byte random IV per encryption (prepended to ciphertext)</li>
 *   <li>128-bit authentication tag</li>
 *   <li>Key derived via PBKDF2WithHmacSHA256 (600,000 iterations per OWASP 2023)</li>
 * </ul>
 */
public class AesCryptoService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final String KEY_ALGORITHM = "AES";
    private static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int PBKDF2_ITERATIONS = 600_000;
    private static final int PBKDF2_KEY_LENGTH = 256;
    private static final byte[] DEFAULT_SALT = "clinvio-ui-persistence-salt-v1".getBytes(StandardCharsets.UTF_8);

    private final SecretKey secretKey;
    private final SecureRandom secureRandom;

    /**
     * Creates a new crypto service with the given passphrase.
     * The passphrase is derived using PBKDF2 with 600,000 iterations.
     *
     * @param passphrase the passphrase to derive the encryption key from
     * @throws IllegalArgumentException if passphrase is null or empty
     */
    public AesCryptoService(String passphrase) {
        this(passphrase, DEFAULT_SALT);
    }

    /**
     * Creates a new crypto service with a custom salt.
     */
    public AesCryptoService(String passphrase, byte[] salt) {
        if (passphrase == null || passphrase.isBlank()) {
            throw new IllegalArgumentException("Encryption passphrase must not be null or empty");
        }
        byte[] effectiveSalt = (salt != null && salt.length > 0) ? salt : DEFAULT_SALT;
        this.secretKey = deriveKey(passphrase, effectiveSalt);
        this.secureRandom = new SecureRandom();
    }

    /**
     * Encrypts plaintext using AES-256-GCM.
     * The returned string is Base64-encoded and contains: IV (12 bytes) + ciphertext + auth tag.
     *
     * @param plaintext the text to encrypt
     * @return Base64-encoded encrypted string, or null if plaintext is null
     */
    public String encrypt(String plaintext) {
        if (plaintext == null) {
            return null;
        }

        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Prepend IV to ciphertext
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + ciphertext.length);
            buffer.put(iv);
            buffer.put(ciphertext);

            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new CryptoException("Failed to encrypt data", e);
        }
    }

    /**
     * Decrypts a Base64-encoded AES-256-GCM encrypted string.
     *
     * @param encryptedText the Base64-encoded encrypted string
     * @return the decrypted plaintext, or null if encryptedText is null
     * @throws CryptoException if decryption fails (tampered data, wrong key, etc.)
     */
    public String decrypt(String encryptedText) {
        if (encryptedText == null) {
            return null;
        }

        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            ByteBuffer buffer = ByteBuffer.wrap(decoded);

            byte[] iv = new byte[GCM_IV_LENGTH];
            buffer.get(iv);

            byte[] ciphertext = new byte[buffer.remaining()];
            buffer.get(ciphertext);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CryptoException("Failed to decrypt data", e);
        }
    }

    /**
     * Checks if a string appears to be encrypted by this service.
     * Returns true if the string is valid Base64 and has the minimum expected length
     * for an encrypted value (IV + at least 1 byte ciphertext + auth tag).
     *
     * @param value the string to check
     * @return true if the value appears to be encrypted
     */
    public boolean isEncrypted(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(value);
            // Minimum: 12 (IV) + 1 (ciphertext) + 16 (GCM tag) = 29 bytes
            return decoded.length >= GCM_IV_LENGTH + 1 + (GCM_TAG_LENGTH / 8);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private SecretKey deriveKey(String passphrase, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    passphrase.toCharArray(),
                    salt,
                    PBKDF2_ITERATIONS,
                    PBKDF2_KEY_LENGTH
            );
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        } catch (Exception e) {
            throw new CryptoException("Failed to derive encryption key", e);
        }
    }

    /**
     * Exception thrown when encryption or decryption operations fail.
     */
    public static class CryptoException extends RuntimeException {
        public CryptoException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
