package hu.clinvio.ui.components.basic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvProgressTest {

    @Test
    void getComponentTypeReturnsProgressBar() {
        CvProgress p = new CvProgress();
        assertEquals("progressBar", p.getComponentType());
    }

    @Test
    void ofFactoryCreatesWithValue() {
        CvProgress p = CvProgress.of(50);
        assertEquals(50, p.getValue());
    }

    @Test
    void fluentSettersReturnThis() {
        CvProgress p = CvProgress.of(50);
        assertSame(p, p.max(200));
        assertSame(p, p.variant("success"));
        assertSame(p, p.striped(true));
        assertSame(p, p.animated(true));
        assertSame(p, p.showLabel(true));
        assertEquals(200, p.getMax());
        assertEquals("success", p.getVariant());
        assertTrue(p.isStriped());
        assertTrue(p.isAnimated());
        assertTrue(p.isShowLabel());
    }

    @Test
    void percentCalculationIsCorrect() {
        CvProgress p = CvProgress.of(50).max(100);
        assertEquals(50, p.getTemplateModel().get("percent"));
    }

    @Test
    void percentWithZeroMaxReturnsZero() {
        CvProgress p = CvProgress.of(50).max(0);
        assertEquals(0, p.getTemplateModel().get("percent"));
    }

    @Test
    void getTemplateReturnsNonNull() {
        CvProgress p = CvProgress.of(0);
        assertNotNull(p.getTemplate());
    }
}
