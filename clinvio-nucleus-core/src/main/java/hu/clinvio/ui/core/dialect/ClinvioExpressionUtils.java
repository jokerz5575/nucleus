package hu.clinvio.ui.core.dialect;

import org.thymeleaf.context.IExpressionContext;

import java.util.Locale;

/**
 * Utility methods available in Thymeleaf templates as ${cv.*}.
 */
public class ClinvioExpressionUtils {

    private final IExpressionContext context;

    public ClinvioExpressionUtils(IExpressionContext context) {
        this.context = context;
    }

    /**
     * Generate a unique component ID.
     */
    public String id(String prefix) {
        return prefix + "-" + Integer.toHexString(System.identityHashCode(context));
    }

    /**
     * Get current locale.
     */
    public Locale locale() {
        return context.getLocale();
    }

    /**
     * Build CSS class string from conditional parts.
     */
    public String cssClass(String... classes) {
        StringBuilder sb = new StringBuilder();
        for (String cls : classes) {
            if (cls != null && !cls.isEmpty()) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(cls);
            }
        }
        return sb.toString();
    }

    /**
     * Conditional CSS class.
     */
    public String cssIf(boolean condition, String trueClass, String falseClass) {
        return condition ? trueClass : (falseClass != null ? falseClass : "");
    }

    /**
     * Generate hx-* attribute values for HTMX integration.
     */
    public String hxGet(String url) {
        return url;
    }

    public String hxPost(String url) {
        return url;
    }

    public String hxTarget(String selector) {
        return selector;
    }

    public String hxSwap(String swapStrategy) {
        return swapStrategy;
    }
}
