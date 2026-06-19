package hu.clinvio.ui.components.form;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CvToggleTest {

    @Test
    void getComponentTypeReturnsToggle() {
        CvToggle toggle = new CvToggle();
        assertEquals("toggle", toggle.getComponentType());
    }

    @Test
    void fluentSettersReturnThisAndSetState() {
        CvToggle toggle = CvToggle.of("Enabled");

        assertSame(toggle, toggle.on(true));
        assertSame(toggle, toggle.disabled(true));
        assertSame(toggle, toggle.variant(CvToggle.ToggleVariant.SUCCESS));
        assertSame(toggle, toggle.labelPosition(CvToggle.LabelPosition.LEFT));

        assertTrue(toggle.isOn());
        assertTrue(toggle.isDisabled());
        assertEquals(CvToggle.ToggleVariant.SUCCESS, toggle.getVariant());
        assertEquals(CvToggle.LabelPosition.LEFT, toggle.getLabelPosition());
    }

    @Test
    void getTemplateModelContainsToggleState() {
        CvToggle toggle = CvToggle.of("Enabled").on(true).variant(CvToggle.ToggleVariant.DANGER);
        var model = toggle.getTemplateModel();

        assertEquals("Enabled", model.get("label"));
        assertEquals(true, model.get("on"));
        assertEquals(CvToggle.ToggleVariant.DANGER, model.get("variant"));
        assertNotNull(toggle.getTemplate());
    }
}
