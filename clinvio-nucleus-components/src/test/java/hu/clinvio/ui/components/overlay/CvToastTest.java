package hu.clinvio.ui.components.overlay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CvToastTest {

    @Test
    void getComponentTypeReturnsToast() {
        CvToast toast = new CvToast();
        assertEquals("toast", toast.getComponentType());
    }

    @Test
    void factorySetsTitleAndMessage() {
        CvToast toast = CvToast.of("Saved", "Changes persisted");

        assertEquals("Saved", toast.getTitle());
        assertEquals("Changes persisted", toast.getMessage());
    }

    @Test
    void fluentSettersReturnThisAndSetState() {
        CvToast toast = CvToast.of("Saved", "Changes persisted");

        assertSame(toast, toast.severity(CvToast.ToastSeverity.SUCCESS));
        assertSame(toast, toast.position(CvToast.ToastPosition.BOTTOM_LEFT));
        assertSame(toast, toast.dismissible(false));
        assertSame(toast, toast.autoDismiss(false));
        assertSame(toast, toast.duration(1000));
        assertSame(toast, toast.icon("custom"));

        assertEquals(CvToast.ToastSeverity.SUCCESS, toast.getSeverity());
        assertEquals(CvToast.ToastPosition.BOTTOM_LEFT, toast.getPosition());
        assertEquals(1000, toast.getDuration());
        assertEquals("custom", toast.getIcon());
    }

    @Test
    void getTemplateModelContainsToastState() {
        CvToast toast = CvToast.of("Saved", "Changes persisted").severity(CvToast.ToastSeverity.SUCCESS);
        var model = toast.getTemplateModel();

        assertEquals("Saved", model.get("title"));
        assertEquals("Changes persisted", model.get("message"));
        assertEquals(CvToast.ToastSeverity.SUCCESS, model.get("severity"));
        assertTrue((Boolean) model.get("dismissible"));
        assertNotNull(toast.getTemplate());
    }
}
