package hu.clinvio.ui.business.workflow;

import java.lang.annotation.*;

/**
 * Defines a valid status transition for workflow state machine.
 * Use on enum values to specify which transitions are allowed.
 *
 * <pre>{@code
 * public enum OrderStatus {
 *     @Transitions(to = {PROCESSING, CANCELLED})
 *     PENDING,
 *
 *     @Transitions(to = {SHIPPED, CANCELLED})
 *     PROCESSING,
 *
 *     @Transitions(to = {DELIVERED})
 *     SHIPPED,
 *
 *     DELIVERED,
 *     CANCELLED
 * }
 * }</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transitions {
    /**
     * The allowed target statuses from this state.
     */
    Class<? extends Enum<?>>[] to() default {};
}
