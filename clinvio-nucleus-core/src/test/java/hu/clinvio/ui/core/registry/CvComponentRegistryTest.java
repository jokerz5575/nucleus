package hu.clinvio.ui.core.registry;

import hu.clinvio.ui.core.component.CvComponent;
import hu.clinvio.ui.core.component.CvComponentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CvComponentRegistryTest {

    private CvComponentRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new CvComponentRegistry();
    }

    @Test
    void registerAndRetrieveComponent() {
        TestComponent component = new TestComponent("test-1");
        registry.register(component);
        Optional<CvComponent> found = registry.getComponent("test-1");
        assertTrue(found.isPresent());
        assertEquals("test-1", found.get().getId());
    }

    @Test
    void registerComponentWithSession() {
        TestComponent component = new TestComponent("session-comp-1");
        registry.register("session-1", component);
        Optional<CvComponent> found = registry.getComponent("session-1", "session-comp-1");
        assertTrue(found.isPresent());
    }

    @Test
    void getComponentReturnsEmptyForMissing() {
        Optional<CvComponent> found = registry.getComponent("non-existent");
        assertFalse(found.isPresent());
    }

    @Test
    void unregisterRemovesComponent() {
        TestComponent component = new TestComponent("to-remove");
        registry.register(component);
        assertTrue(registry.getComponent("to-remove").isPresent());
        registry.unregister("to-remove");
        assertFalse(registry.getComponent("to-remove").isPresent());
    }

    @Test
    void clearSessionRemovesSessionComponents() {
        TestComponent comp1 = new TestComponent("session-a");
        TestComponent comp2 = new TestComponent("session-b");
        registry.register("session-1", comp1);
        registry.register("session-1", comp2);
        registry.clearSession("session-1");
        assertFalse(registry.getComponent("session-a").isPresent());
        assertFalse(registry.getComponent("session-b").isPresent());
    }

    @Test
    void getAllComponentsReturnsCopy() {
        TestComponent comp = new TestComponent("copy-test");
        registry.register(comp);
        Map<String, CvComponent> all = registry.getAllComponents();
        assertEquals(1, all.size());
        assertTrue(all.containsKey("copy-test"));
    }

    @Test
    void getSessionComponentsReturnsCopy() {
        TestComponent comp = new TestComponent("session-copy");
        registry.register("session-x", comp);
        Map<String, CvComponent> session = registry.getSessionComponents("session-x");
        assertEquals(1, session.size());
    }

    @Test
    void getSessionComponentsReturnsEmptyForUnknownSession() {
        Map<String, CvComponent> session = registry.getSessionComponents("unknown");
        assertTrue(session.isEmpty());
    }

    private static class TestComponent implements CvComponent {
        private final String id;

        TestComponent(String id) { this.id = id; }

        @Override public String getComponentType() { return "test"; }
        @Override public String getId() { return id; }
        @Override public String getCssClass() { return null; }
        @Override public boolean isVisible() { return true; }
        @Override public Map<String, String> getAttributes() { return Map.of(); }
        @Override public String getTemplate() { return "test"; }
        @Override public Map<String, Object> getTemplateModel() { return Map.of("id", id); }
    }
}
