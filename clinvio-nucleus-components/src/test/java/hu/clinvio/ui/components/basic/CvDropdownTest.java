package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvDropdownTest {

    @Test
    void getComponentTypeReturnsDropdown() {
        CvDropdown dd = new CvDropdown();
        assertEquals("dropdown", dd.getComponentType());
    }

    @Test
    void ofFactoryCreatesWithTriggerText() {
        CvDropdown dd = CvDropdown.of("Actions");
        assertEquals("Actions", dd.getTriggerText());
    }

    @Test
    void addItemAddDividerAddHeader() {
        CvDropdown dd = CvDropdown.of("Menu");
        dd.addItem("Edit", "/edit");
        dd.addDivider();
        dd.addHeader("Section");
        assertEquals(3, dd.getItems().size());
    }

    @Test
    void dropdownItemProperties() {
        CvDropdown dd = CvDropdown.of("Menu");
        dd.addItem("Edit", "/edit", "bi-pencil");
        dd.addDivider();
        dd.addHeader("Actions");

        var item = dd.getItems().get(0);
        assertEquals("Edit", item.getLabel());
        assertEquals("/edit", item.getUrl());
        assertEquals("bi-pencil", item.getIcon());
        assertFalse(item.isDivider());
        assertFalse(item.isHeader());

        var divider = dd.getItems().get(1);
        assertTrue(divider.isDivider());

        var header = dd.getItems().get(2);
        assertTrue(header.isHeader());
        assertEquals("Actions", header.getLabel());
    }

    @Test
    void getTemplateModelContainsTriggerTextAndItems() {
        CvDropdown dd = CvDropdown.of("Menu");
        dd.addItem("Edit", "/edit");
        var model = dd.getTemplateModel();
        assertEquals("Menu", model.get("triggerText"));
        assertNotNull(model.get("items"));
    }

    @Test
    void triggerIconAndPlacementFluentMethods() {
        CvDropdown dd = CvDropdown.of("Menu");
        assertSame(dd, dd.triggerIcon("bi-gear"));
        assertSame(dd, dd.placement("top-end"));
        assertEquals("bi-gear", dd.getTriggerIcon());
        assertEquals("top-end", dd.getPlacement());
    }
}
