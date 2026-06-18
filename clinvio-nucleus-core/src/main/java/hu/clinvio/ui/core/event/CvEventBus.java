package hu.clinvio.ui.core.event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages event listeners and dispatches events for Clinvio UI components.
 */
public class CvEventBus {

    private final Map<String, List<CvEventListener>> listeners = new ConcurrentHashMap<>();
    private final Map<String, Map<String, CvEventListener>> componentListeners = new ConcurrentHashMap<>();

    /**
     * Register a listener for a specific event type across all components.
     */
    public void addListener(String eventType, CvEventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    /**
     * Register a listener for a specific event on a specific component.
     */
    public void addListener(String componentId, String eventType, CvEventListener listener) {
        componentListeners
                .computeIfAbsent(componentId, k -> new ConcurrentHashMap<>())
                .put(eventType, listener);
    }

    /**
     * Remove a listener for a specific event type.
     */
    public void removeListener(String eventType, CvEventListener listener) {
        List<CvEventListener> typeListeners = listeners.get(eventType);
        if (typeListeners != null) {
            typeListeners.remove(listener);
        }
    }

    /**
     * Dispatch an event to all registered listeners.
     */
    public void dispatch(CvEvent event) {
        // Component-specific listeners
        Map<String, CvEventListener> compListeners = componentListeners.get(event.getComponentId());
        if (compListeners != null) {
            CvEventListener specific = compListeners.get(event.getEventType());
            if (specific != null) {
                specific.onEvent(event);
            }
        }

        // Global event type listeners
        List<CvEventListener> typeListeners = listeners.get(event.getEventType());
        if (typeListeners != null) {
            for (CvEventListener listener : new ArrayList<>(typeListeners)) {
                listener.onEvent(event);
            }
        }
    }

    /**
     * Remove all listeners for a component (e.g., when component is destroyed).
     */
    public void removeComponentListeners(String componentId) {
        componentListeners.remove(componentId);
    }
}
