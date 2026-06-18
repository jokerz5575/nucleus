package hu.clinvio.ui.persistence.annotation;

import hu.clinvio.ui.persistence.crypto.EncryptedStringConverter;
import jakarta.persistence.Convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a String field as sensitive data that is automatically
 * encrypted on write and decrypted on read using AES-256-GCM.
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * @Entity
 * public class Customer extends BaseEntity {
 *
 *     @SensitiveData
 *     private String creditCardNumber;   // encrypted
 *
 *     @SensitiveData
 *     private String email;              // encrypted
 *
 *     private String name;               // NOT encrypted
 * }
 * }</pre>
 *
 * <p>This is a composed annotation that includes
 * {@code @Convert(converter = EncryptedStringConverter.class)},
 * so you only need to write {@code @SensitiveData} on each field.</p>
 *
 * <p>Requires an encryption key configured via:</p>
 * <ul>
 *   <li>{@code clinvio.persistence.encryption-key} property, or</li>
 *   <li>{@code CLINVIO_ENCRYPTION_KEY} environment variable</li>
 * </ul>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Convert(converter = EncryptedStringConverter.class)
public @interface SensitiveData {
}
