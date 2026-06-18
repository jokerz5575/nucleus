package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvAlertTest {

    @Test
    void getComponentTypeReturnsAlert() {
        CvAlert alert = new CvAlert();
        assertEquals("alert", alert.getComponentType());
    }

    @Test
    void ofFactoryCreatesAlertWithMessageAndType() {
        CvAlert alert = CvAlert.of("Something happened", CvAlert.AlertType.WARNING);
        assertEquals("Something happened", alert.getMessage());
        assertEquals(CvAlert.AlertType.WARNING, alert.getType());
    }

    @Test
    void alertTypeHasCorrectCssClassAndDefaultIcon() {
        assertEquals("cv-alert-info", CvAlert.AlertType.INFO.getCssClass());
        assertEquals("bi-info-circle", CvAlert.AlertType.INFO.getDefaultIcon());
        assertEquals("cv-alert-success", CvAlert.AlertType.SUCCESS.getCssClass());
        assertEquals("bi-check-circle", CvAlert.AlertType.SUCCESS.getDefaultIcon());
        assertEquals("cv-alert-warning", CvAlert.AlertType.WARNING.getCssClass());
        assertEquals("bi-exclamation-triangle", CvAlert.AlertType.WARNING.getDefaultIcon());
        assertEquals("cv-alert-danger", CvAlert.AlertType.DANGER.getCssClass());
        assertEquals("bi-x-circle", CvAlert.AlertType.DANGER.getDefaultIcon());
    }

    @Test
    void dismissibleAndIconFluentMethods() {
        CvAlert alert = CvAlert.of("Test", CvAlert.AlertType.INFO);
        assertSame(alert, alert.dismissible(false));
        assertSame(alert, alert.icon("custom-icon"));
        assertFalse(alert.isDismissible());
        assertEquals("custom-icon", alert.getIcon());
    }

    @Test
    void getTemplateModelContainsExpectedKeys() {
        CvAlert alert = CvAlert.of("Test", CvAlert.AlertType.SUCCESS).icon("bi-check");
        var model = alert.getTemplateModel();
        assertEquals("Test", model.get("message"));
        assertEquals(CvAlert.AlertType.SUCCESS, model.get("type"));
        assertEquals("bi-check", model.get("icon"));
    }

    @Test
    void defaultIsDismissible() {
        CvAlert alert = CvAlert.of("Test", CvAlert.AlertType.INFO);
        assertTrue(alert.isDismissible());
    }
}
