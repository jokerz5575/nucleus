package hu.clinvio.ui.components.form;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CvSelectTest {

    @Test
    void getComponentTypeReturnsSelect() {
        CvSelect select = new CvSelect();
        assertEquals("select", select.getComponentType());
    }

    @Test
    void constructorWithLabel() {
        CvSelect select = new CvSelect("Country");
        assertEquals("Country", select.getLabel());
    }

    @Test
    void addOptionCreatesOptionWithValueAndLabel() {
        CvSelect select = new CvSelect("Country");
        select.addOption("us", "United States");
        assertEquals(1, select.getOptions().size());
        assertEquals("us", select.getOptions().get(0).getValue());
        assertEquals("United States", select.getOptions().get(0).getLabel());
    }

    @Test
    void optionsAcceptsList() {
        CvSelect select = new CvSelect("Country");
        var options = List.of(
                new CvSelect.Option("us", "USA"),
                new CvSelect.Option("ca", "Canada")
        );
        select.options(options);
        assertEquals(2, select.getOptions().size());
    }

    @Test
    void optionSelectedProperty() {
        CvSelect.Option option = new CvSelect.Option("us", "USA", true);
        assertTrue(option.isSelected());
        option.setSelected(false);
        assertFalse(option.isSelected());
    }

    @Test
    void getTemplateModelContainsLabelValueOptions() {
        CvSelect select = new CvSelect("Country");
        select.addOption("us", "USA");
        select.addOption("ca", "Canada");
        var model = select.getTemplateModel();
        assertEquals("Country", model.get("label"));
        assertNotNull(model.get("options"));
        assertEquals(2, ((List<?>) model.get("options")).size());
    }
}
