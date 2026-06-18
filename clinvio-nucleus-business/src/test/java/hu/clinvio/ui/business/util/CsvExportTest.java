package hu.clinvio.ui.business.util;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvExportTest {

    static class TestItem {
        private final String name;
        private final int value;

        TestItem(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public int getValue() { return value; }
    }

    @Test
    void builderWithHeadersAndExtractors() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        CsvExport.Builder<TestItem> builder = CsvExport.<TestItem>builder(response, "test");
        assertNotNull(builder);
    }

    @Test
    void writeGeneratesCsvWithHeaders() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        CsvExport.Builder<TestItem> builder = CsvExport.<TestItem>builder(response, "test")
                .addHeader("Name", i -> i.getName())
                .addHeader("Value", i -> String.valueOf(i.getValue()));

        builder.write(List.of(new TestItem("Alice", 100)));

        String csv = response.getContentAsString();
        assertTrue(csv.contains("Name"));
        assertTrue(csv.contains("Value"));
    }

    @Test
    void writeAppliesExtractorsForDataRows() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        CsvExport.Builder<TestItem> builder = CsvExport.<TestItem>builder(response, "test")
                .addHeader("Name", i -> i.getName())
                .addHeader("Value", i -> String.valueOf(i.getValue()));

        builder.write(List.of(new TestItem("Alice", 100), new TestItem("Bob", 200)));

        String csv = response.getContentAsString();
        assertTrue(csv.contains("Alice"));
        assertTrue(csv.contains("Bob"));
        assertTrue(csv.contains("100"));
        assertTrue(csv.contains("200"));
    }

    @Test
    void emptyDataProducesHeaderOnlyCsv() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        CsvExport.Builder<TestItem> builder = CsvExport.<TestItem>builder(response, "test")
                .addHeader("Name", i -> i.getName());

        builder.write(List.of());

        String csv = response.getContentAsString();
        assertTrue(csv.contains("Name"));
    }

    @Test
    void specialCharactersInValuesAreHandled() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        CsvExport.Builder<TestItem> builder = CsvExport.<TestItem>builder(response, "test")
                .addHeader("Name", i -> i.getName());

        builder.write(List.of(new TestItem("Alice, \"the great\"", 0)));

        String csv = response.getContentAsString();
        assertTrue(csv.contains("\"Alice, \"\"the great\"\"\""));
    }
}
