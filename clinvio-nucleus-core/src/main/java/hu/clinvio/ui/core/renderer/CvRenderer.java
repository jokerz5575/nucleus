package hu.clinvio.ui.core.renderer;

import hu.clinvio.ui.core.component.CvComponent;
import hu.clinvio.ui.core.registry.CvComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

public class CvRenderer {

    private static final Logger log = LoggerFactory.getLogger(CvRenderer.class);

    private final TemplateEngine templateEngine;
    private final CvComponentRegistry componentRegistry;
    private final String templatePrefix;

    public CvRenderer(TemplateEngine templateEngine, CvComponentRegistry componentRegistry) {
        this(templateEngine, componentRegistry, "");
    }

    public CvRenderer(TemplateEngine templateEngine, CvComponentRegistry componentRegistry, String templatePrefix) {
        this.templateEngine = templateEngine;
        this.componentRegistry = componentRegistry;
        this.templatePrefix = templatePrefix != null ? templatePrefix : "";
    }

    public String render(CvComponent component) {
        return render(component, Locale.getDefault());
    }

    public String render(CvComponent component, Locale locale) {
        Context context = new Context(locale);
        context.setVariables(component.getTemplateModel());

        String template = component.getTemplate();
        String resolvedTemplate = templatePrefix + template;
        log.debug("Rendering component {} with template {}", component.getId(), resolvedTemplate);

        try {
            return templateEngine.process(resolvedTemplate, context);
        } catch (Exception e) {
            log.error("Failed to render component {} with template {}", component.getId(), resolvedTemplate, e);
            return "<div class=\"cv-render-error\">Component render error: " + e.getMessage() + "</div>";
        }
    }

    public String renderById(String componentId) {
        return componentRegistry.getComponent(componentId)
                .map(this::render)
                .orElse("<div class=\"cv-render-error\">Component not found: " + componentId + "</div>");
    }

    public String renderById(String componentId, Locale locale) {
        return componentRegistry.getComponent(componentId)
                .map(c -> render(c, locale))
                .orElse("<div class=\"cv-render-error\">Component not found: " + componentId + "</div>");
    }

    public String renderFragment(String templateName, Map<String, Object> model) {
        return renderFragment(templateName, model, Locale.getDefault());
    }

    public String renderFragment(String templateName, Map<String, Object> model, Locale locale) {
        Context context = new Context(locale);
        context.setVariables(model);

        String resolvedTemplate = templatePrefix + templateName;

        try {
            return templateEngine.process(resolvedTemplate, context);
        } catch (Exception e) {
            log.error("Failed to render fragment {}", resolvedTemplate, e);
            return "<div class=\"cv-render-error\">Fragment render error: " + e.getMessage() + "</div>";
        }
    }
}
