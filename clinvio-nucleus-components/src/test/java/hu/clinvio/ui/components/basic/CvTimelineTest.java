package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvTimelineTest {

    @Test
    void getComponentTypeReturnsTimeline() {
        CvTimeline tl = new CvTimeline();
        assertEquals("timeline", tl.getComponentType());
    }

    @Test
    void ofFactoryReturnsInstance() {
        CvTimeline tl = CvTimeline.of();
        assertNotNull(tl);
    }

    @Test
    void addItemWithTitleDescriptionTimeIcon() {
        CvTimeline tl = CvTimeline.of();
        tl.addItem("Deployed", "Version 2.0", "2025-01-15", "bi-rocket");
        assertEquals(1, tl.getItems().size());
        assertEquals("Deployed", tl.getItems().get(0).getTitle());
        assertEquals("Version 2.0", tl.getItems().get(0).getDescription());
        assertEquals("2025-01-15", tl.getItems().get(0).getTime());
        assertEquals("bi-rocket", tl.getItems().get(0).getIcon());
        assertNull(tl.getItems().get(0).getStatus());
    }

    @Test
    void addItemWithStatusParameter() {
        CvTimeline tl = CvTimeline.of();
        tl.addItem("Created", "Order placed", "2025-01-10", "bi-cart", "completed");
        assertEquals("completed", tl.getItems().get(0).getStatus());
    }

    @Test
    void getTemplateModelContainsItems() {
        CvTimeline tl = CvTimeline.of();
        tl.addItem("Event", "Desc", "2025-01-01", "bi-star");
        var model = tl.getTemplateModel();
        assertNotNull(model.get("items"));
        assertEquals(1, ((java.util.List<?>) model.get("items")).size());
    }
}
