package hu.clinvio.ui.business.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Utility class for internationalization (i18n).
 *
 * <pre>{@code
 * @Autowired
 * private CvMessages messages;
 *
 * // Get message for current locale
 * String msg = messages.get("order.created");
 *
 * // Get message with arguments
 * String msg = messages.get("order.status.changed", "ORD-001", "Shipped");
 *
 * // Get message for specific locale
 * String msg = messages.get("order.created", Locale.ENGLISH);
 * }</pre>
 */
@Component
public class CvMessages {

    private final MessageSource messageSource;

    public CvMessages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get a message for the current locale.
     */
    public String get(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    /**
     * Get a message with arguments for the current locale.
     */
    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    /**
     * Get a message for a specific locale.
     */
    public String get(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    /**
     * Get a message with a default value if not found.
     */
    public String getOrDefault(String key, String defaultValue) {
        return messageSource.getMessage(key, null, defaultValue, LocaleContextHolder.getLocale());
    }

    /**
     * Get current locale.
     */
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }
}
