package hu.clinvio.ui.business.util;

import hu.clinvio.ui.business.exception.CvValidationException;
import hu.clinvio.ui.business.validation.CvValidationResult;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Utility class for common validation patterns.
 *
 * <p>Use in service or controller methods:</p>
 * <pre>{@code
 * CvValidation.validateNotBlank(name, "name", "Name is required");
 * CvValidation.validateEmail(email, "email");
 * CvValidation.validateMinLength(password, 8, "password");
 * CvValidation.validateNotNull(customer, "customer");
 *
 * // Or build validation result manually:
 * CvValidationResult result = CvValidation.builder()
 *     .require(name, "name", "Name is required")
 *     .requireEmail(email, "email")
 *     .requireMinLength(password, 8, "password")
 *     .build();
 * if (result.hasErrors()) throw new CvValidationException(result);
 * }</pre>
 */
public class CvValidation {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[0-9]{10,15}$"
    );

    private CvValidation() {}

    // ==================== Single Field Validators ====================

    /**
     * Validate that a value is not null or blank.
     */
    public static void validateNotBlank(String value, String field, String message) {
        if (value == null || value.isBlank()) {
            throw new CvValidationException(message,
                    new CvValidationResult() {{ addError(field, message); }});
        }
    }

    /**
     * Validate that a value is not null.
     */
    public static void validateNotNull(Object value, String field, String message) {
        if (value == null) {
            throw new CvValidationException(message,
                    new CvValidationResult() {{ addError(field, message); }});
        }
    }

    /**
     * Validate email format.
     */
    public static void validateEmail(String email, String field) {
        if (email != null && !email.isBlank() && !EMAIL_PATTERN.matcher(email).matches()) {
            throw new CvValidationException("Invalid email format",
                    new CvValidationResult() {{ addError(field, "Invalid email format"); }});
        }
    }

    /**
     * Validate phone number format.
     */
    public static void validatePhone(String phone, String field) {
        if (phone != null && !phone.isBlank() && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new CvValidationException("Invalid phone number",
                    new CvValidationResult() {{ addError(field, "Invalid phone number format"); }});
        }
    }

    /**
     * Validate minimum length.
     */
    public static void validateMinLength(String value, int minLength, String field) {
        if (value != null && value.length() < minLength) {
            throw new CvValidationException(
                    String.format("Must be at least %d characters", minLength),
                    new CvValidationResult() {{ addError(field, String.format("Must be at least %d characters", minLength)); }}
            );
        }
    }

    /**
     * Validate maximum length.
     */
    public static void validateMaxLength(String value, int maxLength, String field) {
        if (value != null && value.length() > maxLength) {
            throw new CvValidationException(
                    String.format("Must be at most %d characters", maxLength),
                    new CvValidationResult() {{ addError(field, String.format("Must be at most %d characters", maxLength)); }}
            );
        }
    }

    /**
     * Validate value is in range.
     */
    public static <T extends Comparable<T>> void validateInRange(T value, T min, T max, String field) {
        if (value != null && (value.compareTo(min) < 0 || value.compareTo(max) > 0)) {
            throw new CvValidationException(
                    String.format("Must be between %s and %s", min, max),
                    new CvValidationResult() {{ addError(field, String.format("Must be between %s and %s", min, max)); }}
            );
        }
    }

    /**
     * Validate collection is not empty.
     */
    public static void validateNotEmpty(Collection<?> collection, String field, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new CvValidationException(message,
                    new CvValidationResult() {{ addError(field, message); }});
        }
    }

    // ==================== Builder Pattern ====================

    /**
     * Create a validation builder for accumulating multiple validations.
     */
    public static ValidationBuilder builder() {
        return new ValidationBuilder();
    }

    /**
     * Builder for accumulating validation errors.
     */
    public static class ValidationBuilder {
        private final CvValidationResult result = new CvValidationResult();

        public ValidationBuilder require(String value, String field, String message) {
            if (value == null || value.isBlank()) {
                result.addError(field, message);
            }
            return this;
        }

        public ValidationBuilder requireNotNull(Object value, String field, String message) {
            if (value == null) {
                result.addError(field, message);
            }
            return this;
        }

        public ValidationBuilder requireEmail(String email, String field) {
            if (email != null && !email.isBlank() && !EMAIL_PATTERN.matcher(email).matches()) {
                result.addError(field, "Invalid email format");
            }
            return this;
        }

        public ValidationBuilder requirePhone(String phone, String field) {
            if (phone != null && !phone.isBlank() && !PHONE_PATTERN.matcher(phone).matches()) {
                result.addError(field, "Invalid phone number format");
            }
            return this;
        }

        public ValidationBuilder requireMinLength(String value, int minLength, String field) {
            if (value != null && value.length() < minLength) {
                result.addError(field, String.format("Must be at least %d characters", minLength));
            }
            return this;
        }

        public ValidationBuilder requireMaxLength(String value, int maxLength, String field) {
            if (value != null && value.length() > maxLength) {
                result.addError(field, String.format("Must be at most %d characters", maxLength));
            }
            return this;
        }

        public ValidationBuilder requirePositive(Number value, String field) {
            if (value != null && value.doubleValue() <= 0) {
                result.addError(field, "Must be greater than zero");
            }
            return this;
        }

        public ValidationBuilder custom(boolean condition, String field, String message) {
            if (condition) {
                result.addError(field, message);
            }
            return this;
        }

        public CvValidationResult build() {
            return result;
        }

        public void throwIfInvalid() {
            if (result.hasErrors()) {
                throw new CvValidationException("Validation failed", result);
            }
        }
    }
}
