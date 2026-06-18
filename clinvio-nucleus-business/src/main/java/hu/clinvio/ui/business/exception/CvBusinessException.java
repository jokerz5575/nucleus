package hu.clinvio.ui.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when a business rule is violated.
 * Returns HTTP 422 Unprocessable Entity status.
 *
 * <p>Use this for domain-specific validation that goes beyond field-level input validation:</p>
 * <pre>{@code
 * if (order.getStatus() == OrderStatus.DELIVERED) {
 *     throw new CvBusinessException("Cannot cancel a delivered order");
 * }
 * }</pre>
 */
public class CvBusinessException extends ResponseStatusException {

    private final String errorCode;

    public CvBusinessException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, message);
        this.errorCode = null;
    }

    public CvBusinessException(String errorCode, String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, message);
        this.errorCode = errorCode;
    }

    public CvBusinessException(HttpStatus status, String message) {
        super(status, message);
        this.errorCode = null;
    }

    public CvBusinessException(HttpStatus status, String errorCode, String message) {
        super(status, message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
