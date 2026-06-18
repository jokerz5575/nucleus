package hu.clinvio.ui.business.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Form submission result with validation errors.
 * Used to return form processing results to the client.
 */
public class CvFormResult {

    private boolean success;
    private String message;
    private String errorCode;
    private String redirectUrl;
    private Object data;
    private Map<String, String> errors = new HashMap<>();
    private long timestamp;

    public CvFormResult() {
        this.timestamp = System.currentTimeMillis();
    }

    private CvFormResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Create a successful result.
     */
    public static CvFormResult ok(String message) {
        return new CvFormResult(true, message);
    }

    /**
     * Create a successful result with redirect.
     */
    public static CvFormResult ok(String message, String redirectUrl) {
        CvFormResult result = new CvFormResult(true, message);
        result.redirectUrl = redirectUrl;
        return result;
    }

    /**
     * Create a successful result with data.
     */
    public static <T> CvFormResult ok(String message, T data) {
        CvFormResult result = new CvFormResult(true, message);
        result.data = data;
        return result;
    }

    /**
     * Create a failed result.
     */
    public static CvFormResult error(String message) {
        return new CvFormResult(false, message);
    }

    /**
     * Create a failed result with field errors.
     */
    public static CvFormResult error(String message, Map<String, String> errors) {
        CvFormResult result = new CvFormResult(false, message);
        result.errors = errors;
        return result;
    }

    /**
     * Create a failed result with error code.
     */
    public static CvFormResult error(String errorCode, String message, boolean includeCode) {
        CvFormResult result = new CvFormResult(false, message);
        result.errorCode = errorCode;
        return result;
    }

    /**
     * Add a field error.
     */
    public CvFormResult addError(String field, String message) {
        this.errors.put(field, message);
        this.success = false;
        return this;
    }

    /**
     * Check if there are any errors.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
