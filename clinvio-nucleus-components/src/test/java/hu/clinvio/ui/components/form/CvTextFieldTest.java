package hu.clinvio.ui.components.form;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvTextFieldTest {

    @Test
    void getComponentTypeReturnsTextField() {
        CvTextField field = new CvTextField();
        assertEquals("textField", field.getComponentType());
    }

    @Test
    void staticFactoriesSetCorrectType() {
        assertEquals("text", CvTextField.text("Name").getType());
        assertEquals("email", CvTextField.email("Email").getType());
        assertEquals("password", CvTextField.password("Password").getType());
        assertEquals("number", CvTextField.number("Count").getType());
        assertEquals("date", CvTextField.date("Date").getType());
    }

    @Test
    void fluentSettersReturnThis() {
        CvTextField field = new CvTextField();
        assertSame(field, field.value("test"));
        assertSame(field, field.placeholder("Enter..."));
        assertSame(field, field.required(true));
        assertSame(field, field.disabled(true));
        assertEquals("test", field.getValue());
        assertEquals("Enter...", field.getPlaceholder());
        assertTrue(field.isRequired());
        assertTrue(field.isDisabled());
    }

    @Test
    void helpTextStateErrorMessageFluentMethods() {
        CvTextField field = CvTextField.text("Name");
        assertSame(field, field.helpText("Your name"));
        assertSame(field, field.state(CvTextField.FieldState.SUCCESS));
        assertSame(field, field.errorMessage("Invalid"));
        assertEquals("Your name", field.getHelpText());
        assertEquals(CvTextField.FieldState.ERROR, field.getState());
        assertEquals("Invalid", field.getErrorMessage());
    }

    @Test
    void fieldStateEnumCssClasses() {
        assertEquals("", CvTextField.FieldState.DEFAULT.getCssClass());
        assertEquals("cv-field-success", CvTextField.FieldState.SUCCESS.getCssClass());
        assertEquals("cv-field-error", CvTextField.FieldState.ERROR.getCssClass());
    }

    @Test
    void errorMessageSetsStateToError() {
        CvTextField field = CvTextField.text("Name");
        field.errorMessage("Required");
        assertEquals(CvTextField.FieldState.ERROR, field.getState());
    }

    @Test
    void getTemplateModelContainsAllExpectedKeys() {
        CvTextField field = CvTextField.email("Email")
                .value("a@b.com")
                .placeholder("you@example.com")
                .required(true);
        var model = field.getTemplateModel();
        assertEquals("Email", model.get("label"));
        assertEquals("a@b.com", model.get("value"));
        assertEquals("email", model.get("type"));
        assertTrue((Boolean) model.get("required"));
        assertNotNull(model.get("state"));
    }

    @Test
    void defaultTypeIsText() {
        CvTextField field = new CvTextField();
        assertEquals("text", field.getType());
    }
}
