package hu.clinvio.ui.components.data;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CvDataTableTest {

    @Test
    void getComponentTypeReturnsDataTable() {
        CvDataTable<?> table = new CvDataTable<>();
        assertEquals("dataTable", table.getComponentType());
    }

    @Test
    void addColumnWithKeyAndHeader() {
        CvDataTable<String> table = new CvDataTable<>();
        table.addColumn("name", "Name");
        assertEquals(1, table.getColumns().size());
        assertEquals("name", table.getColumns().get(0).getKey());
        assertEquals("Name", table.getColumns().get(0).getHeader());
    }

    @Test
    void addColumnWithSortableFlag() {
        CvDataTable<String> table = new CvDataTable<>();
        table.addColumn("name", "Name", true);
        assertTrue(table.getColumns().get(0).isSortable());
        table.addColumn("age", "Age", false);
        assertFalse(table.getColumns().get(1).isSortable());
    }

    @Test
    void dataAcceptsList() {
        CvDataTable<Map<String, String>> table = new CvDataTable<>();
        var rows = List.of(
                Map.of("name", "Alice"),
                Map.of("name", "Bob")
        );
        table.data(rows);
        assertEquals(2, table.getData().size());
    }

    @Test
    void searchableSortablePageableFluentMethods() {
        CvDataTable<String> table = new CvDataTable<>();
        assertSame(table, table.searchable(true));
        assertSame(table, table.pageable(true));
        assertTrue(table.isSearchable());
        assertTrue(table.isPageable());
    }

    @Test
    void pageSizeThrowsOnInvalid() {
        CvDataTable<String> table = new CvDataTable<>();
        assertThrows(IllegalArgumentException.class, () -> table.pageSize(0));
        assertThrows(IllegalArgumentException.class, () -> table.pageSize(-1));
    }

    @Test
    void paginationCalculations() {
        CvDataTable<String> table = new CvDataTable<>();
        table.pageable(true);
        table.pageSize(10);
        table.setTotalItems(25);
        assertEquals(3, table.getTotalPages());
        assertFalse(table.isHasPrevious());
        assertTrue(table.isHasNext());
        table.setCurrentPage(1);
        assertTrue(table.isHasPrevious());
        assertTrue(table.isHasNext());
        table.setCurrentPage(2);
        assertTrue(table.isHasPrevious());
        assertFalse(table.isHasNext());
    }

    @Test
    void getTemplateModelContainsColumnsDataPagination() {
        CvDataTable<String> table = new CvDataTable<>();
        table.addColumn("id", "ID");
        table.data(List.of("item1"));
        table.pageable(true).pageSize(5);
        table.setTotalItems(1);
        var model = table.getTemplateModel();
        assertNotNull(model.get("columns"));
        assertNotNull(model.get("data"));
        assertTrue(model.containsKey("pageable"));
        assertTrue(model.containsKey("totalPages"));
        assertTrue(model.containsKey("hasPrevious"));
        assertTrue(model.containsKey("hasNext"));
        assertEquals(1, model.get("totalPages"));
    }
}
