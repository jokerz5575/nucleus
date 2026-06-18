package hu.clinvio.ui.core.component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract base implementation of CvComponent with common properties.
 */
public abstract class AbstractCvComponent implements CvComponent {

    private String id;
    private String cssClass;
    private boolean visible = true;
    private Map<String, String> attributes = new LinkedHashMap<>();
    private Map<String, Object> properties = new LinkedHashMap<>();

    protected AbstractCvComponent() {
        this.id = generateId();
    }

    protected AbstractCvComponent(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("component", this);
        model.put("id", id);
        model.put("cssClass", cssClass);
        model.put("visible", visible);
        model.put("attributes", attributes);
        model.putAll(properties);
        return model;
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

    private String generateId() {
        return "cv-" + getComponentType() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
