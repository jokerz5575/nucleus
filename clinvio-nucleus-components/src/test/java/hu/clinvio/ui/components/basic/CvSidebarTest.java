package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CvSidebarTest {

    @Test
    void getComponentTypeReturnsSidebar() {
        CvSidebar sidebar = new CvSidebar();
        assertEquals("sidebar", sidebar.getComponentType());
    }

    @Test
    void fluentSettersReturnThisAndSetState() {
        CvSidebar sidebar = CvSidebar.create();
        var items = List.of(new CvSidebar.SidebarItem("Home", "/"));

        assertSame(sidebar, sidebar.items(items));
        assertSame(sidebar, sidebar.collapsed(true));

        assertEquals(items, sidebar.getItems());
        assertTrue(sidebar.isCollapsed());
    }

    @Test
    void sidebarItemSupportsIconActiveAndSubItems() {
        CvSidebar.SidebarItem item = new CvSidebar.SidebarItem("Settings", "/settings")
                .icon("gear")
                .active(true)
                .addSubItem(new CvSidebar.SidebarItem("Users", "/settings/users"));

        assertEquals("gear", item.getIcon());
        assertTrue(item.isActive());
        assertEquals(1, item.getSubItems().size());
    }

    @Test
    void getTemplateModelContainsSidebarState() {
        CvSidebar sidebar = CvSidebar.create()
                .addItem(new CvSidebar.SidebarItem("Home", "/"))
                .collapsed(true);
        var model = sidebar.getTemplateModel();

        assertEquals(1, ((List<?>) model.get("items")).size());
        assertEquals(true, model.get("collapsed"));
        assertNotNull(sidebar.getTemplate());
    }
}
