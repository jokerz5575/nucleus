package hu.clinvio.ui.core.registry;

import hu.clinvio.ui.core.component.CvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing component instances and their metadata.
 * Components are registered per-session scope.
 */
public class CvComponentRegistry {

    private final Map<String, CvComponent> components = new ConcurrentHashMap<>();
    private final Map<String, Map<String, CvComponent>> sessionComponents = new ConcurrentHashMap<>();

    /**
     * Register a component instance.
     */
    public void register(CvComponent component) {
        components.put(component.getId(), component);
    }

    /**
     * Register a component instance within a session scope.
     */
    public void register(String sessionId, CvComponent component) {
        sessionComponents.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>())
                .put(component.getId(), component);
        components.put(component.getId(), component);
    }

    /**
     * Get a component by its ID.
     */
    public Optional<CvComponent> getComponent(String componentId) {
        return Optional.ofNullable(components.get(componentId));
    }

    /**
     * Get a component by ID and session scope.
     */
    public Optional<CvComponent> getComponent(String sessionId, String componentId) {
        Map<String, CvComponent> session = sessionComponents.get(sessionId);
        if (session != null) {
            CvComponent component = session.get(componentId);
            if (component != null) {
                return Optional.of(component);
            }
        }
        return Optional.ofNullable(components.get(componentId));
    }

    /**
     * Remove a component from the registry.
     */
    public void unregister(String componentId) {
        components.remove(componentId);
        sessionComponents.values().forEach(session -> session.remove(componentId));
    }

    /**
     * Remove all components for a session (e.g., on session timeout).
     */
    public void clearSession(String sessionId) {
        Map<String, CvComponent> session = sessionComponents.remove(sessionId);
        if (session != null) {
            session.keySet().forEach(components::remove);
        }
    }

    /**
     * Get all registered components.
     */
    public Map<String, CvComponent> getAllComponents() {
        return Map.copyOf(components);
    }

    /**
     * Get all components for a session.
     */
    public Map<String, CvComponent> getSessionComponents(String sessionId) {
        Map<String, CvComponent> session = sessionComponents.get(sessionId);
        return session != null ? Map.copyOf(session) : Map.of();
    }
}
