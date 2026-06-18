package hu.clinvio.ui.core.renderer;

import hu.clinvio.ui.core.component.CvComponent;
import hu.clinvio.ui.core.registry.CvComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

/**
 * Renders Clinvio UI components to HTML fragments.
 * Used for both full page rendering and HTMX partial updates.
 */
public class CvRenderer {

    private static final Logger log = LoggerFactory.getLogger(CvRenderer.class);

    private final TemplateEngine templateEngine;
    private final CvComponentRegistry componentRegistry;

    public CvRenderer(TemplateEngine templateEngine, CvComponentRegistry componentRegistry) {
        this.templateEngine = templateEngine;
        this.componentRegistry = componentRegistry;
    }

    /**
     * Render a component to an HTML string.
     */
    public String render(CvComponent component) {
        return render(component, Locale.getDefault());
    }

    /**
     * Render a component to an HTML string with a specific locale.
     */
    public String render(CvComponent component, Locale locale) {
        Context context = new Context(locale);
        context.setVariables(component.getTemplateModel());

        String template = component.getTemplate();
        log.debug("Rendering component {} with template {}", component.getId(), template);

        try {
            return templateEngine.process(template, context);
        } catch (Exception e) {
            log.error("Failed to render component {} with template {}", component.getId(), template, e);
            return "<div class=\"cv-render-error\">Component render error: " + e.getMessage() + "</div>";
        }
    }

    /**
     * Render a component by its ID.
     */
    public String renderById(String componentId) {
        return componentRegistry.getComponent(componentId)
                .map(this::render)
                .orElse("<div class=\"cv-render-error\">Component not found: " + componentId + "</div>");
    }

    /**
     * Render a component by its ID with a specific locale.
     */
    public String renderById(String componentId, Locale locale) {
        return componentRegistry.getComponent(componentId)
                .map(c -> render(c, locale))
                .orElse("<div class=\"cv-render-error\">Component not found: " + componentId + "</div>");
    }

    /**
     * Render a named template fragment (for partial page updates).
     */
    public String renderFragment(String templateName, Map<String, Object> model) {
        return renderFragment(templateName, model, Locale.getDefault());
    }

    /**
     * Render a named template fragment with locale.
     */
    public String renderFragment(String templateName, Map<String, Object> model, Locale locale) {
        Context context = new Context(locale);
        context.setVariables(model);

        try {
            return templateEngine.process(templateName, context);
        } catch (Exception e) {
            log.error("Failed to render fragment {}", templateName, e);
            return "<div class=\"cv-render-error\">Fragment render error: " + e.getMessage() + "</div>";
        }
    }
}
