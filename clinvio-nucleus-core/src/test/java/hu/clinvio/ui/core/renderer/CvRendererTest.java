package hu.clinvio.ui.core.renderer;

import hu.clinvio.ui.core.component.CvComponent;
import hu.clinvio.ui.core.registry.CvComponentRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CvRendererTest {

    private TemplateEngine templateEngine;
    private CvComponentRegistry registry;

    @BeforeEach
    void setUp() {
        templateEngine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode("HTML");
        templateEngine.setTemplateResolver(resolver);
        registry = new CvComponentRegistry();
    }

    @Test
    void renderReturnsContentForComponentWithTemplate() {
        CvRenderer renderer = new CvRenderer(templateEngine, registry);
        String result = renderer.render(new TestComponent("test-1"));
        assertNotNull(result);
    }

    @Test
    void renderByIdReturnsComponentContent() {
        TestComponent component = new TestComponent("by-id");
        registry.register(component);

        CvRenderer renderer = new CvRenderer(templateEngine, registry);
        String result = renderer.renderById("by-id");
        assertNotNull(result);
    }

    @Test
    void renderByIdReturnsErrorForMissing() {
        CvRenderer renderer = new CvRenderer(templateEngine, registry);
        String result = renderer.renderById("missing");
        assertTrue(result.contains("Component not found"));
    }

    @Test
    void renderFragmentProcessesTemplate() {
        CvRenderer renderer = new CvRenderer(templateEngine, registry);
        Map<String, Object> model = Map.of("message", "hello");
        assertNotNull(renderer.renderFragment("test-fragment", model));
    }

    @Test
    void constructorWithTemplatePrefixStoresIt() {
        CvRenderer renderer = new CvRenderer(templateEngine, registry, "prefix/");
        String result = renderer.renderFragment("test", Map.of());
        assertNotNull(result);
    }

    @Test
    void renderFragmentUsesGenericErrorByDefault() {
        CvRenderer renderer = new CvRenderer(missingTemplateEngine(), registry);

        String result = renderer.renderFragment("broken", Map.of());

        assertEquals("<div class=\"cv-render-error\">Fragment render error</div>", result);
    }

    @Test
    void renderFragmentIncludesEscapedDetailsWhenEnabled() {
        CvRenderer renderer = new CvRenderer(missingTemplateEngine(), registry, "", true);

        String result = renderer.renderFragment("broken", Map.of());

        assertTrue(result.startsWith("<div class=\"cv-render-error\">Fragment render error: "));
        assertFalse(result.contains("<script>"));
    }

    private TemplateEngine missingTemplateEngine() {
        TemplateEngine engine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("missing/");
        resolver.setSuffix(".html");
        resolver.setCheckExistence(true);
        engine.setTemplateResolver(resolver);
        return engine;
    }

    private static class TestComponent implements CvComponent {
        private final String id;

        TestComponent(String id) { this.id = id; }

        @Override public String getComponentType() { return "test"; }
        @Override public String getId() { return id; }
        @Override public String getCssClass() { return null; }
        @Override public boolean isVisible() { return true; }
        @Override public Map<String, String> getAttributes() { return Map.of(); }
        @Override public String getTemplate() { return "test-template"; }
        @Override public Map<String, Object> getTemplateModel() { return Map.of("id", id); }
    }
}
