package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvBreadcrumbTest {

    @Test
    void getComponentTypeReturnsBreadcrumb() {
        CvBreadcrumb bc = new CvBreadcrumb();
        assertEquals("breadcrumb", bc.getComponentType());
    }

    @Test
    void ofFactoryReturnsInstance() {
        CvBreadcrumb bc = CvBreadcrumb.of();
        assertNotNull(bc);
    }

    @Test
    void addItemAddsBreadcrumbItem() {
        CvBreadcrumb bc = CvBreadcrumb.of();
        bc.addItem("Home", "/");
        assertEquals(1, bc.getItems().size());
        assertEquals("Home", bc.getItems().get(0).getLabel());
        assertEquals("/", bc.getItems().get(0).getUrl());
        assertFalse(bc.getItems().get(0).isActive());
    }

    @Test
    void addActiveCreatesActiveItem() {
        CvBreadcrumb bc = CvBreadcrumb.of();
        bc.addActive("Current");
        assertEquals(1, bc.getItems().size());
        assertTrue(bc.getItems().get(0).isActive());
        assertNull(bc.getItems().get(0).getUrl());
    }

    @Test
    void getTemplateModelContainsItems() {
        CvBreadcrumb bc = CvBreadcrumb.of();
        bc.addItem("Home", "/");
        bc.addActive("Page");
        var model = bc.getTemplateModel();
        assertNotNull(model.get("items"));
        assertEquals(2, ((java.util.List<?>) model.get("items")).size());
    }
}
