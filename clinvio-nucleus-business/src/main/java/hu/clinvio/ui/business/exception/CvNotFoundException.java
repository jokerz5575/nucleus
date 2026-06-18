package hu.clinvio.ui.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when a requested entity is not found.
 * Returns HTTP 404 status.
 */
public class CvNotFoundException extends ResponseStatusException {

    public CvNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public CvNotFoundException(String entityName, Object id) {
        super(HttpStatus.NOT_FOUND, entityName + " not found with id: " + id);
    }
}
