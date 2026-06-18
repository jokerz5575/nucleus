package hu.clinvio.ui.components.overlay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvModalTest {

    @Test
    void getComponentTypeReturnsModal() {
        CvModal modal = new CvModal();
        assertEquals("modal", modal.getComponentType());
    }

    @Test
    void ofFactoryCreatesWithTitle() {
        CvModal modal = CvModal.of("Confirm Delete");
        assertEquals("Confirm Delete", modal.getTitle());
    }

    @Test
    void fluentSettersReturnThis() {
        CvModal modal = CvModal.of("Title");
        assertSame(modal, modal.content("<p>Content</p>"));
        assertSame(modal, modal.size("lg"));
        assertSame(modal, modal.closable(false));
        assertSame(modal, modal.backdrop(false));
        assertEquals("<p>Content</p>", modal.getContent());
        assertEquals("lg", modal.getSize());
        assertFalse(modal.isClosable());
        assertFalse(modal.isBackdrop());
    }

    @Test
    void confirmTextCancelTextConfirmAction() {
        CvModal modal = CvModal.of("Title");
        assertSame(modal, modal.confirmText("Yes"));
        assertSame(modal, modal.cancelText("No"));
        assertSame(modal, modal.confirmAction("/confirm"));
        assertEquals("Yes", modal.getConfirmText());
        assertEquals("No", modal.getCancelText());
        assertEquals("/confirm", modal.getConfirmAction());
    }

    @Test
    void getTemplateModelContainsTitleContentSize() {
        CvModal modal = CvModal.of("My Modal")
                .content("Hello")
                .size("sm");
        var model = modal.getTemplateModel();
        assertEquals("My Modal", model.get("title"));
        assertEquals("Hello", model.get("content"));
        assertEquals("sm", model.get("size"));
    }

    @Test
    void defaultClosableIsTrue() {
        CvModal modal = new CvModal();
        assertTrue(modal.isClosable());
    }
}
