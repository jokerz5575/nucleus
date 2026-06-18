package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvTabsTest {

    @Test
    void getComponentTypeReturnsTabs() {
        CvTabs tabs = new CvTabs();
        assertEquals("tabs", tabs.getComponentType());
    }

    @Test
    void ofFactoryReturnsInstance() {
        CvTabs tabs = CvTabs.of();
        assertNotNull(tabs);
    }

    @Test
    void addTabWithIdLabelContent() {
        CvTabs tabs = CvTabs.of();
        tabs.addTab("tab1", "First", "Content 1");
        assertEquals(1, tabs.getTabs().size());
        assertEquals("tab1", tabs.getTabs().get(0).getId());
        assertEquals("First", tabs.getTabs().get(0).getLabel());
        assertEquals("Content 1", tabs.getTabs().get(0).getContent());
    }

    @Test
    void activeIndexFluentMethod() {
        CvTabs tabs = CvTabs.of();
        assertSame(tabs, tabs.activeIndex(2));
        assertEquals(2, tabs.getActiveIndex());
    }

    @Test
    void getTemplateModelContainsTabsAndActiveIndex() {
        CvTabs tabs = CvTabs.of();
        tabs.addTab("a", "A", "Content A");
        tabs.addTab("b", "B", "Content B");
        tabs.activeIndex(1);
        var model = tabs.getTemplateModel();
        assertNotNull(model.get("tabs"));
        assertEquals(1, model.get("activeIndex"));
    }
}
