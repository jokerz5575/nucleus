package hu.clinvio.ui.security.annotation;

import java.lang.annotation.*;

/**
 * Annotation to mark methods or classes that require authentication.
 *
 * <pre>{@code
 * @Secured
 * @GetMapping("/admin")
 * public String adminPage() { ... }
 *
 * @Secured(roles = {"ADMIN", "MANAGER"})
 * @PostMapping("/users")
 * public HxResponse createUser() { ... }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Secured {
    /**
     * Allowed roles. If empty, just requires authentication.
     */
    String[] roles() default {};
}
