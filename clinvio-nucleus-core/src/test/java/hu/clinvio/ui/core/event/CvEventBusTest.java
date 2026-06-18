package hu.clinvio.ui.core.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CvEventBusTest {

    private CvEventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new CvEventBus();
    }

    @Test
    void globalListenerReceivesEvent() {
        AtomicBoolean received = new AtomicBoolean(false);
        eventBus.addListener("click", e -> received.set(true));

        CvEvent event = new CvEvent("comp-1", "click", Map.of());
        eventBus.dispatch(event);

        assertTrue(received.get());
    }

    @Test
    void componentSpecificListenerReceivesEvent() {
        AtomicBoolean received = new AtomicBoolean(false);
        eventBus.addListener("comp-1", "click", e -> received.set(true));

        CvEvent event = new CvEvent("comp-1", "click", Map.of());
        eventBus.dispatch(event);

        assertTrue(received.get());
    }

    @Test
    void componentSpecificListenerDoesNotReceiveWrongComponent() {
        AtomicBoolean received = new AtomicBoolean(false);
        eventBus.addListener("comp-1", "click", e -> received.set(true));

        CvEvent event = new CvEvent("comp-2", "click", Map.of());
        eventBus.dispatch(event);

        assertFalse(received.get());
    }

    @Test
    void removeListenerStopsReceivingEvents() {
        AtomicInteger count = new AtomicInteger(0);
        CvEventListener listener = e -> count.incrementAndGet();

        eventBus.addListener("click", listener);
        eventBus.dispatch(new CvEvent("c1", "click", Map.of()));
        assertEquals(1, count.get());

        eventBus.removeListener("click", listener);
        eventBus.dispatch(new CvEvent("c2", "click", Map.of()));
        assertEquals(1, count.get());
    }

    @Test
    void multipleGlobalListenersAllReceive() {
        AtomicInteger count = new AtomicInteger(0);
        eventBus.addListener("change", e -> count.incrementAndGet());
        eventBus.addListener("change", e -> count.incrementAndGet());

        eventBus.dispatch(new CvEvent("c1", "change", Map.of()));
        assertEquals(2, count.get());
    }

    @Test
    void removeComponentListenersCleansUp() {
        AtomicBoolean received = new AtomicBoolean(false);
        eventBus.addListener("comp-1", "click", e -> received.set(true));
        eventBus.removeComponentListeners("comp-1");

        eventBus.dispatch(new CvEvent("comp-1", "click", Map.of()));
        assertFalse(received.get());
    }

    @Test
    void eventCarriesComponentId() {
        CvEvent event = new CvEvent("my-component", "focus", Map.of());
        assertEquals("my-component", event.getComponentId());
    }

    @Test
    void eventCarriesEventType() {
        CvEvent event = new CvEvent("c1", "blur", Map.of());
        assertEquals("blur", event.getEventType());
    }

    @Test
    void eventCarriesParameters() {
        CvEvent event = new CvEvent("c1", "keyup", Map.of("key", "Enter"));
        assertEquals("Enter", event.getParameter("key"));
        assertEquals("default", event.getParameter("missing", "default"));
    }

    @Test
    void eventCarriesPayload() {
        String payload = "test-payload";
        CvEvent event = new CvEvent("c1", "custom", Map.of(), payload);
        assertEquals(payload, event.getPayload());
    }

    @Test
    void eventWithNullParametersUsesEmptyMap() {
        CvEvent event = new CvEvent("c1", "test", null);
        assertTrue(event.getParameters().isEmpty());
    }

    @Test
    void dispatchWithNoListenersDoesNotThrow() {
        CvEvent event = new CvEvent("c1", "unknown", Map.of());
        assertDoesNotThrow(() -> eventBus.dispatch(event));
    }
}
