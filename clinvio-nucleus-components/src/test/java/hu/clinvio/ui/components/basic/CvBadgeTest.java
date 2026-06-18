package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvBadgeTest {

    @Test
    void primaryContainsCvBadgePrimary() {
        String html = CvBadge.primary("Active");
        assertTrue(html.contains("cv-badge-primary"));
        assertTrue(html.contains("Active"));
    }

    @Test
    void successContainsCvBadgeSuccess() {
        String html = CvBadge.success("Done");
        assertTrue(html.contains("cv-badge-success"));
    }

    @Test
    void dangerContainsCvBadgeDanger() {
        String html = CvBadge.danger("Blocked");
        assertTrue(html.contains("cv-badge-danger"));
    }

    @Test
    void fromStatusUsesProvidedClass() {
        String html = CvBadge.fromStatus("Custom", "my-custom-class");
        assertTrue(html.contains("my-custom-class"));
        assertTrue(html.contains("Custom"));
    }

    @Test
    void htmlEscapingWorks() {
        String html = CvBadge.primary("<script>");
        assertTrue(html.contains("&lt;script&gt;"));
        assertFalse(html.contains("<script>"));
    }

    @Test
    void nullTextReturnsEmpty() {
        String html = CvBadge.primary(null);
        assertTrue(html.contains("cv-badge-primary"));
    }
}
