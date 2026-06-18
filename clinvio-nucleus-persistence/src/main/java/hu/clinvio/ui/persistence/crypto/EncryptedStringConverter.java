package hu.clinvio.ui.persistence.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * JPA attribute converter that automatically encrypts and decrypts
 * String fields marked with {@link hu.clinvio.ui.persistence.annotation.SensitiveData}.
 *
 * <p>When a field is written to the database, the plaintext is encrypted using AES-256-GCM.
 * When a field is read from the database, the ciphertext is decrypted back to plaintext.</p>
 *
 * <p><strong>Column sizing:</strong> AES-256-GCM with Base64 encoding expands data by ~35%
 * plus a fixed 44-byte overhead (12 IV + 16 tag + Base64 padding). Ensure your database
 * column is large enough. For example, a 100-char plaintext needs at least a 200-char column.</p>
 */
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static final Logger log = LoggerFactory.getLogger(EncryptedStringConverter.class);

    private static volatile AesCryptoService cryptoService;

    /**
     * Injected by Spring. Uses static field so the JPA provider can instantiate
     * the converter without Spring context, but encryption still works via the
     * static reference set during application startup.
     */
    @Autowired
    public void setCryptoService(AesCryptoService cryptoService) {
        EncryptedStringConverter.cryptoService = cryptoService;
        log.debug("EncryptedStringConverter initialized with AES crypto service");
    }

    /**
     * Converts a Java attribute value (plaintext) to its database representation (ciphertext).
     *
     * @param attribute the plaintext value to encrypt
     * @return the Base64-encoded encrypted value, or null if attribute is null
     * @throws IllegalStateException if cryptoService is not initialized
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        if (cryptoService == null) {
            throw new IllegalStateException(
                    "Cannot encrypt sensitive data: AesCryptoService not initialized. " +
                    "Ensure clinvio.persistence.encryption-key or CLINVIO_ENCRYPTION_KEY is configured.");
        }
        return cryptoService.encrypt(attribute);
    }

    /**
     * Converts a database value (ciphertext) to its Java attribute representation (plaintext).
     *
     * @param dbData the Base64-encoded encrypted value from the database
     * @return the decrypted plaintext, or null if dbData is null
     * @throws IllegalStateException if cryptoService is not initialized
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        if (cryptoService == null) {
            throw new IllegalStateException(
                    "Cannot decrypt sensitive data: AesCryptoService not initialized. " +
                    "Ensure clinvio.persistence.encryption-key or CLINVIO_ENCRYPTION_KEY is configured.");
        }
        return cryptoService.decrypt(dbData);
    }
}
