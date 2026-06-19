package hu.clinvio.ui.components.form;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CvCheckboxTest {

    @Test
    void getComponentTypeReturnsCheckbox() {
        CvCheckbox checkbox = new CvCheckbox();
        assertEquals("checkbox", checkbox.getComponentType());
    }

    @Test
    void fluentSettersReturnThisAndSetState() {
        CvCheckbox checkbox = CvCheckbox.of("Accept");

        assertSame(checkbox, checkbox.checked(true));
        assertSame(checkbox, checkbox.indeterminate(true));
        assertSame(checkbox, checkbox.disabled(true));
        assertSame(checkbox, checkbox.helpText("Required"));
        assertSame(checkbox, checkbox.errorMessage("Missing"));

        assertTrue(checkbox.isChecked());
        assertTrue(checkbox.isIndeterminate());
        assertTrue(checkbox.isDisabled());
        assertEquals("Required", checkbox.getHelpText());
        assertEquals("Missing", checkbox.getErrorMessage());
        assertEquals(CvTextField.FieldState.ERROR, checkbox.getState());
    }

    @Test
    void getTemplateModelContainsCheckboxState() {
        CvCheckbox checkbox = CvCheckbox.of("Accept").checked(true).helpText("Required");
        var model = checkbox.getTemplateModel();

        assertEquals("Accept", model.get("label"));
        assertEquals(true, model.get("checked"));
        assertEquals("Required", model.get("helpText"));
        assertNotNull(checkbox.getTemplate());
    }
}
