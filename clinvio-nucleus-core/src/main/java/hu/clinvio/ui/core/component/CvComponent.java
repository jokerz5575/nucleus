package hu.clinvio.ui.core.component;

import java.util.Map;

/**
 * Base interface for all Clinvio UI components.
 * Components are server-side Java objects that render to HTML via Thymeleaf templates.
 */
public interface CvComponent {

    /**
     * Unique component type identifier (e.g., "button", "card", "dataTable").
     */
    String getComponentType();

    /**
     * Unique ID for this component instance.
     */
    String getId();

    /**
     * CSS class names to apply to the root element.
     */
    String getCssClass();

    /**
     * Whether the component is visible.
     */
    boolean isVisible();

    /**
     * Additional HTML attributes for the root element.
     */
    Map<String, String> getAttributes();

    /**
     * Template path for rendering this component (relative to templates/cv/).
     */
    String getTemplate();

    /**
     * Model data to pass to the Thymeleaf template.
     */
    Map<String, Object> getTemplateModel();
}
