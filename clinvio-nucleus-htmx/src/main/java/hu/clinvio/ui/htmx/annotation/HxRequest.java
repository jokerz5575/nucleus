package hu.clinvio.ui.htmx.annotation;

import java.lang.annotation.*;

/**
 * Annotation to mark a controller method as handling HTMX requests.
 * Can be used to detect HTMX requests and return partial HTML responses.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HxRequest {

    /**
     * The target element ID to swap content into.
     */
    String target() default "";

    /**
     * The swap strategy (innerHTML, outerHTML, beforeend, etc.).
     */
    String swap() default "innerHTML";

    /**
     * Trigger event after swap.
     */
    String trigger() default "";
}
