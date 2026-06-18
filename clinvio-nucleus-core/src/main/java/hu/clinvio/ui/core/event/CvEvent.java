package hu.clinvio.ui.core.event;

import java.util.Map;

/**
 * Represents an event triggered by a Clinvio UI component.
 */
public class CvEvent {

    private final String componentId;
    private final String eventType;
    private final Map<String, String> parameters;
    private final Object payload;

    public CvEvent(String componentId, String eventType, Map<String, String> parameters) {
        this(componentId, eventType, parameters, null);
    }

    public CvEvent(String componentId, String eventType, Map<String, String> parameters, Object payload) {
        this.componentId = componentId;
        this.eventType = eventType;
        this.parameters = parameters != null ? parameters : Map.of();
        this.payload = payload;
    }

    public String getComponentId() {
        return componentId;
    }

    public String getEventType() {
        return eventType;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Object getPayload() {
        return payload;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public String getParameter(String name, String defaultValue) {
        return parameters.getOrDefault(name, defaultValue);
    }
}
