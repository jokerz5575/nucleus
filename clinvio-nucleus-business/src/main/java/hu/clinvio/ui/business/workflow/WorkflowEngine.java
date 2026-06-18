package hu.clinvio.ui.business.workflow;

import hu.clinvio.ui.business.exception.CvBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Workflow engine for managing entity status transitions.
 *
 * <p>Provides a state machine pattern for entity lifecycle management.
 * Define valid transitions and the engine enforces them.</p>
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * // Define workflow
 * WorkflowEngine<OrderStatus> workflow = WorkflowEngine.of(OrderStatus.class)
 *     .transition(OrderStatus.PENDING, OrderStatus.PROCESSING)
 *     .transition(OrderStatus.PENDING, OrderStatus.CANCELLED)
 *     .transition(OrderStatus.PROCESSING, OrderStatus.SHIPPED)
 *     .transition(OrderStatus.PROCESSING, OrderStatus.CANCELLED)
 *     .transition(OrderStatus.SHIPPED, OrderStatus.DELIVERED)
 *     .onTransition(OrderStatus.PROCESSING, OrderStatus.SHIPPED, (from, to) -> {
 *         notificationService.sendShippingNotification(order);
 *     })
 *     .build();
 *
 * // Validate transition
 * workflow.validate(OrderStatus.PENDING, OrderStatus.PROCESSING); // OK
 * workflow.validate(OrderStatus.DELIVERED, OrderStatus.PENDING); // throws CvBusinessException
 *
 * // Execute transition with callbacks
 * workflow.transition(order, OrderStatus.PROCESSING, order::setStatus);
 * }</pre>
 *
 * @param <E> the status enum type
 */
public class WorkflowEngine<E extends Enum<E>> {

    private static final Logger log = LoggerFactory.getLogger(WorkflowEngine.class);

    private final Class<E> enumClass;
    private final Map<E, Set<E>> transitions = new ConcurrentHashMap<>();
    private final Map<String, List<TransitionAction<E>>> callbacks = new ConcurrentHashMap<>();
    private final Map<E, List<TransitionAction<E>>> onEnterCallbacks = new ConcurrentHashMap<>();
    private final Map<E, List<TransitionAction<E>>> onLeaveCallbacks = new ConcurrentHashMap<>();

    private WorkflowEngine(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Create a new workflow builder for the given enum type.
     */
    public static <T extends Enum<T>> WorkflowEngine<T> of(Class<T> enumClass) {
        return new WorkflowEngine<>(enumClass);
    }

    /**
     * Add a valid transition.
     */
    public WorkflowEngine<E> transition(E from, E to) {
        transitions.computeIfAbsent(from, k -> new HashSet<>()).add(to);
        return this;
    }

    /**
     * Add multiple valid transitions from a single state.
     */
    public WorkflowEngine<E> transition(E from, E... to) {
        Set<E> targets = transitions.computeIfAbsent(from, k -> new HashSet<>());
        targets.addAll(Arrays.asList(to));
        return this;
    }

    /**
     * Add a callback for a specific transition.
     */
    public WorkflowEngine<E> onTransition(E from, E to, TransitionAction<E> action) {
        String key = from.name() + "->" + to.name();
        callbacks.computeIfAbsent(key, k -> new ArrayList<>()).add(action);
        return this;
    }

    /**
     * Add a callback for entering a state.
     */
    public WorkflowEngine<E> onEnter(E state, TransitionAction<E> action) {
        onEnterCallbacks.computeIfAbsent(state, k -> new ArrayList<>()).add(action);
        return this;
    }

    /**
     * Add a callback for leaving a state.
     */
    public WorkflowEngine<E> onLeave(E state, TransitionAction<E> action) {
        onLeaveCallbacks.computeIfAbsent(state, k -> new ArrayList<>()).add(action);
        return this;
    }

    /**
     * Build the workflow engine.
     */
    public WorkflowEngine<E> build() {
        return this;
    }

    /**
     * Validate that a transition is allowed.
     *
     * @throws CvBusinessException if the transition is not allowed
     */
    public void validate(E from, E to) {
        if (!isValidTransition(from, to)) {
            throw new CvBusinessException("WORKFLOW_INVALID_TRANSITION",
                    String.format("Cannot transition from %s to %s", from, to));
        }
    }

    /**
     * Check if a transition is valid.
     */
    public boolean isValidTransition(E from, E to) {
        Set<E> allowed = transitions.get(from);
        return allowed != null && allowed.contains(to);
    }

    /**
     * Get all valid target states from a given state.
     */
    public Set<E> getValidTransitions(E from) {
        return transitions.getOrDefault(from, Collections.emptySet());
    }

    /**
     * Execute a transition with validation and callbacks.
     *
     * @param entity    the entity being transitioned
     * @param newStatus the target status
     * @param setter    the function to set the status on the entity
     * @return the new status
     * @throws CvBusinessException if the transition is not allowed
     */
    public <T> E transition(T entity, E newStatus, java.util.function.Consumer<E> setter) {
        E currentStatus = getCurrentStatus(entity);
        validate(currentStatus, newStatus);

        // Execute onLeave callbacks
        executeCallbacks(onLeaveCallbacks.get(currentStatus), currentStatus, newStatus);

        // Execute transition callbacks
        String key = currentStatus.name() + "->" + newStatus.name();
        executeCallbacks(callbacks.get(key), currentStatus, newStatus);

        // Set new status
        setter.accept(newStatus);

        // Execute onEnter callbacks
        executeCallbacks(onEnterCallbacks.get(newStatus), currentStatus, newStatus);

        log.debug("Transitioned {} from {} to {}", entity.getClass().getSimpleName(), currentStatus, newStatus);
        return newStatus;
    }

    /**
     * Get the current status of an entity using reflection.
     */
    @SuppressWarnings("unchecked")
    private <T> E getCurrentStatus(T entity) {
        try {
            var method = entity.getClass().getMethod("getStatus");
            return (E) method.invoke(entity);
        } catch (Exception e) {
            throw new CvBusinessException("Cannot read status from entity: " + e.getMessage());
        }
    }

    private void executeCallbacks(List<TransitionAction<E>> actions, E from, E to) {
        if (actions != null) {
            for (TransitionAction<E> action : actions) {
                try {
                    action.execute(from, to);
                } catch (Exception e) {
                    log.error("Error executing transition callback from {} to {}: {}", from, to, e.getMessage());
                    throw new CvBusinessException("WORKFLOW_CALLBACK_ERROR",
                            "Error during status transition: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Functional interface for transition callbacks.
     */
    @FunctionalInterface
    public interface TransitionAction<E> {
        void execute(E from, E to) throws Exception;
    }
}
