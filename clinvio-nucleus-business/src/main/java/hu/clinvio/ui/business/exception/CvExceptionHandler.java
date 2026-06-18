package hu.clinvio.ui.business.exception;

import hu.clinvio.ui.business.dto.CvFormResult;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for Clinvio controllers.
 * Provides consistent error responses for all exception types.
 */
@ControllerAdvice
public class CvExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CvExceptionHandler.class);

    /**
     * Handle not-found exceptions.
     */
    @ExceptionHandler(CvNotFoundException.class)
    public ResponseEntity<CvFormResult> handleNotFound(CvNotFoundException ex, HttpServletRequest request) {
        log.warn("Not found: {}", ex.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CvFormResult.error(ex.getReason()));
    }

    /**
     * Handle duplicate entity exceptions.
     */
    @ExceptionHandler(CvDuplicateException.class)
    public ResponseEntity<CvFormResult> handleDuplicate(CvDuplicateException ex, HttpServletRequest request) {
        log.warn("Duplicate: {}", ex.getReason());
        CvFormResult result = CvFormResult.error(ex.getReason());
        if (ex.getField() != null) {
            result.addError(ex.getField(), ex.getReason());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    /**
     * Handle business rule violations.
     */
    @ExceptionHandler(CvBusinessException.class)
    public ResponseEntity<CvFormResult> handleBusiness(CvBusinessException ex, HttpServletRequest request) {
        log.warn("Business rule violated [{}]: {}", ex.getErrorCode(), ex.getReason());
        CvFormResult result = CvFormResult.error(ex.getReason());
        if (ex.getErrorCode() != null) {
            result.setErrorCode(ex.getErrorCode());
        }
        return ResponseEntity.status(ex.getStatusCode()).body(result);
    }

    /**
     * Handle validation exceptions.
     */
    @ExceptionHandler(CvValidationException.class)
    public ResponseEntity<CvFormResult> handleValidation(CvValidationException ex, HttpServletRequest request) {
        log.warn("Validation failed: {}", ex.getReason());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CvFormResult.error(
                        ex.getReason() != null ? ex.getReason() : "Validation failed",
                        ex.getValidationResult().toSimpleMap()
                ));
    }

    /**
     * Handle crypto exceptions (safe messages only).
     */
    @ExceptionHandler(CvCryptoException.class)
    public ResponseEntity<CvFormResult> handleCrypto(CvCryptoException ex, HttpServletRequest request) {
        log.error("Crypto error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CvFormResult.error(ex.getReason()));
    }

    /**
     * Handle missing request parameters.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CvFormResult> handleMissingParam(MissingServletRequestParameterException ex) {
        log.warn("Missing parameter: {}", ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CvFormResult.error("Missing required parameter: " + ex.getParameterName())
                        .addError(ex.getParameterName(), "This field is required"));
    }

    /**
     * Handle type mismatch errors (e.g., passing "abc" for a Long parameter).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CvFormResult> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch: {}", ex.getName());
        String message = String.format("Invalid value '%s' for parameter '%s'",
                ex.getValue(), ex.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CvFormResult.error(message));
    }

    /**
     * Handle unsupported HTTP methods.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CvFormResult> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.warn("Method not allowed: {}", ex.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(CvFormResult.error("Method " + ex.getMethod() + " is not supported"));
    }

    /**
     * Handle static resource not found.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CvFormResult> handleNoResource(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CvFormResult.error("Resource not found"));
    }

    /**
     * Handle Bean Validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CvFormResult> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        CvFormResult result = CvFormResult.error("Validation failed");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                result.addError(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    /**
     * Handle generic ResponseStatusException.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CvFormResult> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        log.error("Request failed: {} - {}", ex.getStatusCode(), ex.getReason());
        return ResponseEntity.status(ex.getStatusCode())
                .body(CvFormResult.error(ex.getReason() != null ? ex.getReason() : "An error occurred"));
    }

    /**
     * Handle unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CvFormResult> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CvFormResult.error("An unexpected error occurred. Please try again later."));
    }
}
