package hu.clinvio.ui.core.component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvComponentTypeTest {

    @Test
    void allTypesHaveNonEmptyType() {
        for (CvComponentType type : CvComponentType.values()) {
            assertNotNull(type.getType());
            assertFalse(type.getType().isEmpty());
        }
    }

    @Test
    void allTypesHaveNonEmptyTemplate() {
        for (CvComponentType type : CvComponentType.values()) {
            assertNotNull(type.getDefaultTemplate());
            assertFalse(type.getDefaultTemplate().isEmpty());
        }
    }

    @Test
    void fromTypeReturnsCorrectEnum() {
        assertEquals(CvComponentType.BUTTON, CvComponentType.fromType("button").orElseThrow());
        assertEquals(CvComponentType.CARD, CvComponentType.fromType("card").orElseThrow());
        assertEquals(CvComponentType.MODAL, CvComponentType.fromType("modal").orElseThrow());
    }

    @Test
    void fromTypeReturnsEmptyOnUnknown() {
        assertTrue(CvComponentType.fromType("unknown").isEmpty());
    }

    @Test
    void buttonHasCorrectTemplate() {
        assertEquals("components/button", CvComponentType.BUTTON.getDefaultTemplate());
    }

    @Test
    void modalHasCorrectTemplate() {
        assertEquals("components/modal", CvComponentType.MODAL.getDefaultTemplate());
    }

    @Test
    void componentTypeIsUnique() {
        for (int i = 0; i < CvComponentType.values().length; i++) {
            for (int j = i + 1; j < CvComponentType.values().length; j++) {
                assertNotEquals(CvComponentType.values()[i].getType(), CvComponentType.values()[j].getType());
            }
        }
    }

    @Test
    void templatePathIsUnique() {
        for (int i = 0; i < CvComponentType.values().length; i++) {
            for (int j = i + 1; j < CvComponentType.values().length; j++) {
                assertNotEquals(CvComponentType.values()[i].getDefaultTemplate(), CvComponentType.values()[j].getDefaultTemplate());
            }
        }
    }

    @Test
    void allTypesAre23() {
        assertEquals(23, CvComponentType.values().length);
    }
}
