package hu.clinvio.ui.business.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validation error container for HTMX form re-rendering.
 * Collects field-level and global validation errors.
 */
public class CvValidationResult {

    private final Map<String, List<String>> fieldErrors = new HashMap<>();
    private final List<String> globalErrors = new ArrayList<>();

    /**
     * Add a field-level error.
     *
     * @param field   the field name
     * @param message the error message
     */
    public void addError(String field, String message) {
        fieldErrors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }

    /**
     * Add a global error (not tied to a specific field).
     *
     * @param message the error message
     */
    public void addGlobalError(String message) {
        globalErrors.add(message);
    }

    /**
     * Check if there are any errors.
     */
    public boolean hasErrors() {
        return !fieldErrors.isEmpty() || !globalErrors.isEmpty();
    }

    /**
     * Get the first error message for a field.
     *
     * @param field the field name
     * @return the first error message, or null if no errors for this field
     */
    public String getFirstError(String field) {
        List<String> errors = fieldErrors.get(field);
        return (errors != null && !errors.isEmpty()) ? errors.get(0) : null;
    }

    /**
     * Get all error messages for a field.
     *
     * @param field the field name
     * @return list of error messages, empty list if no errors
     */
    public List<String> getErrors(String field) {
        return fieldErrors.getOrDefault(field, List.of());
    }

    /**
     * Get all field errors as a map of field -> first error message.
     * Useful for simple form binding.
     */
    public Map<String, String> toSimpleMap() {
        Map<String, String> result = new HashMap<>();
        fieldErrors.forEach((field, errors) -> {
            if (!errors.isEmpty()) {
                result.put(field, errors.get(0));
            }
        });
        return result;
    }

    /**
     * Merge another validation result into this one.
     */
    public void merge(CvValidationResult other) {
        other.fieldErrors.forEach((field, errors) ->
                fieldErrors.computeIfAbsent(field, k -> new ArrayList<>()).addAll(errors));
        globalErrors.addAll(other.globalErrors);
    }

    // Getters
    public Map<String, List<String>> getFieldErrors() { return fieldErrors; }
    public List<String> getGlobalErrors() { return globalErrors; }
}
