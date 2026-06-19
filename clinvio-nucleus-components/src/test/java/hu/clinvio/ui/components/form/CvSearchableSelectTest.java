package hu.clinvio.ui.components.form;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CvSearchableSelectTest {

    @Test
    void getComponentTypeReturnsSearchableSelect() {
        CvSearchableSelect select = new CvSearchableSelect();
        assertEquals("searchableSelect", select.getComponentType());
    }

    @Test
    void addOptionCreatesOptionWithValueAndLabel() {
        CvSearchableSelect select = CvSearchableSelect.of("Country").addOption("hu", "Hungary");

        assertEquals(1, select.getOptions().size());
        assertEquals("hu", select.getOptions().get(0).getValue());
        assertEquals("Hungary", select.getOptions().get(0).getLabel());
    }

    @Test
    void fluentSettersReturnThisAndSetState() {
        CvSearchableSelect select = CvSearchableSelect.of("Country");
        var options = List.of(new CvSelect.Option("hu", "Hungary"));

        assertSame(select, select.placeholder("Search"));
        assertSame(select, select.options(options));
        assertSame(select, select.disabled(true));
        assertSame(select, select.helpText("Type to filter"));

        assertEquals("Search", select.getPlaceholder());
        assertEquals(options, select.getOptions());
        assertTrue(select.isDisabled());
        assertEquals("Type to filter", select.getHelpText());
    }

    @Test
    void getTemplateModelContainsSearchableSelectState() {
        CvSearchableSelect select = CvSearchableSelect.of("Country").placeholder("Search").addOption("hu", "Hungary");
        var model = select.getTemplateModel();

        assertEquals("Country", model.get("label"));
        assertEquals("Search", model.get("placeholder"));
        assertEquals(1, ((List<?>) model.get("options")).size());
        assertNotNull(select.getTemplate());
    }
}
