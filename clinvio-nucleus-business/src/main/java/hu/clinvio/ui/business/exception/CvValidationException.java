package hu.clinvio.ui.business.exception;

import hu.clinvio.ui.business.validation.CvValidationResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when validation fails.
 * Returns HTTP 400 status with field errors.
 */
public class CvValidationException extends ResponseStatusException {

    private final CvValidationResult validationResult;

    public CvValidationException(String message, CvValidationResult validationResult) {
        super(HttpStatus.BAD_REQUEST, message);
        this.validationResult = validationResult;
    }

    public CvValidationException(CvValidationResult validationResult) {
        super(HttpStatus.BAD_REQUEST, "Validation failed");
        this.validationResult = validationResult;
    }

    public CvValidationResult getValidationResult() {
        return validationResult;
    }
}
