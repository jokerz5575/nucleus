package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvButtonTest {

    @Test
    void getComponentTypeReturnsButton() {
        CvButton button = new CvButton();
        assertEquals("button", button.getComponentType());
    }

    @Test
    void staticFactoryMethodsSetCorrectType() {
        assertEquals(CvButton.ButtonType.PRIMARY, CvButton.primary("Primary").getType());
        assertEquals(CvButton.ButtonType.SECONDARY, CvButton.secondary("Secondary").getType());
        assertEquals(CvButton.ButtonType.SUCCESS, CvButton.success("Success").getType());
        assertEquals(CvButton.ButtonType.DANGER, CvButton.danger("Danger").getType());
    }

    @Test
    void fluentSettersReturnThisAndSetState() {
        CvButton button = new CvButton();
        assertSame(button, button.icon("bi-check"));
        assertSame(button, button.size(CvButton.ButtonSize.LARGE));
        assertSame(button, button.disabled(true));
        assertSame(button, button.loading(true));
        assertEquals("bi-check", button.getIcon());
        assertEquals(CvButton.ButtonSize.LARGE, button.getSize());
        assertTrue(button.isDisabled());
        assertTrue(button.isLoading());
    }

    @Test
    void hxMethodsSetValues() {
        CvButton button = new CvButton();
        button.hxGet("/api/data");
        button.hxPost("/api/save");
        button.hxTarget("#result");
        button.hxSwap("innerHTML");
        assertEquals("/api/data", button.getHxGet());
        assertEquals("/api/save", button.getHxPost());
        assertEquals("#result", button.getHxTarget());
        assertEquals("innerHTML", button.getHxSwap());
    }

    @Test
    void getTemplateModelContainsAllExpectedKeys() {
        CvButton button = CvButton.primary("Click");
        button.icon("bi-star").disabled(true);
        var model = button.getTemplateModel();
        assertTrue(model.containsKey("label"));
        assertTrue(model.containsKey("type"));
        assertTrue(model.containsKey("size"));
        assertTrue(model.containsKey("icon"));
        assertTrue(model.containsKey("disabled"));
        assertTrue(model.containsKey("loading"));
        assertEquals("Click", model.get("label"));
        assertEquals(CvButton.ButtonType.PRIMARY, model.get("type"));
        assertTrue((Boolean) model.get("disabled"));
    }

    @Test
    void defaultValuesArePrimaryAndDefault() {
        CvButton button = new CvButton();
        assertEquals(CvButton.ButtonType.PRIMARY, button.getType());
        assertEquals(CvButton.ButtonSize.DEFAULT, button.getSize());
    }

    @Test
    void getTemplateReturnsNonNull() {
        CvButton button = new CvButton();
        assertNotNull(button.getTemplate());
    }

    @Test
    void getIdIsNotNullAfterConstruction() {
        CvButton button = new CvButton();
        assertNotNull(button.getId());
    }
}
