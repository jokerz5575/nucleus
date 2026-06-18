package hu.clinvio.ui.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when encryption or decryption operations fail.
 * Returns HTTP 500 Internal Server Error status.
 *
 * <p>This exception wraps underlying crypto errors and provides
 * safe error messages that don't leak sensitive information.</p>
 */
public class CvCryptoException extends ResponseStatusException {

    public CvCryptoException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public CvCryptoException(String message, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    /**
     * Create a safe exception that doesn't expose crypto details.
     */
    public static CvCryptoException encryptionFailed() {
        return new CvCryptoException("Failed to secure data. Please contact support.");
    }

    /**
     * Create a safe exception that doesn't expose crypto details.
     */
    public static CvCryptoException decryptionFailed() {
        return new CvCryptoException("Failed to read secured data. Please contact support.");
    }
}
