package hu.clinvio.ui.autoconfigure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClinvioPropertiesTest {

    @Test
    void defaultValues_shouldBeCorrect() {
        ClinvioProperties props = new ClinvioProperties();

        assertTrue(props.isEnabled());
        assertEquals("light", props.getTheme().getDefaultTheme());
        assertTrue(props.getTheme().isSwitchable());
        assertTrue(props.getTheme().isCookieBased());
        assertTrue(props.getHtmx().isEnabled());
        assertEquals("2.0.4", props.getHtmx().getVersion());
        assertEquals("innerHTML", props.getHtmx().getDefaultSwap());
        assertEquals("cv", props.getComponents().getIdPrefix());
        assertTrue(props.getComponents().isStateful());
        assertEquals("", props.getComponents().getTemplatePrefix());
        assertFalse(props.getComponents().isRenderErrorDetails());
    }

    @Test
    void nestedObjects_shouldBeCreated() {
        ClinvioProperties props = new ClinvioProperties();

        assertNotNull(props.getTheme());
        assertNotNull(props.getHtmx());
        assertNotNull(props.getComponents());
        assertNotNull(props.getComponents().getPagination());
        assertNotNull(props.getComponents().getProcessors());
    }

    @Test
    void setters_shouldWorkCorrectly() {
        ClinvioProperties props = new ClinvioProperties();

        props.setEnabled(false);
        assertFalse(props.isEnabled());

        props.getTheme().setDefaultTheme("dark");
        assertEquals("dark", props.getTheme().getDefaultTheme());

        props.getTheme().setSwitchable(false);
        assertFalse(props.getTheme().isSwitchable());

        props.getHtmx().setEnabled(false);
        assertFalse(props.getHtmx().isEnabled());

        props.getComponents().setIdPrefix("app");
        assertEquals("app", props.getComponents().getIdPrefix());

        props.getComponents().setTemplatePrefix("/templates/");
        assertEquals("/templates/", props.getComponents().getTemplatePrefix());

        props.getComponents().setRenderErrorDetails(true);
        assertTrue(props.getComponents().isRenderErrorDetails());
    }

    @Test
    void paginationDefaults_shouldBe10And100() {
        ClinvioProperties.Components.Pagination pagination =
                new ClinvioProperties().getComponents().getPagination();

        assertEquals(10, pagination.getDefaultPageSize());
        assertEquals(100, pagination.getMaxPageSize());
    }

    @Test
    void processorDefaults_shouldBe1000_1000_900() {
        ClinvioProperties.Components.Processors processors =
                new ClinvioProperties().getComponents().getProcessors();

        assertEquals(1000, processors.getButton());
        assertEquals(1000, processors.getComponent());
        assertEquals(900, processors.getRender());
    }
}
