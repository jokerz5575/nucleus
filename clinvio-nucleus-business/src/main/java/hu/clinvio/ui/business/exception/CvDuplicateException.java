package hu.clinvio.ui.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when attempting to create a duplicate entity.
 * Returns HTTP 409 Conflict status.
 */
public class CvDuplicateException extends ResponseStatusException {

    private final String field;
    private final String value;

    public CvDuplicateException(String entityName, String field, String value) {
        super(HttpStatus.CONFLICT, String.format("%s already exists with %s: %s", entityName, field, value));
        this.field = field;
        this.value = value;
    }

    public CvDuplicateException(String message) {
        super(HttpStatus.CONFLICT, message);
        this.field = null;
        this.value = null;
    }

    public String getField() { return field; }
    public String getValue() { return value; }
}
