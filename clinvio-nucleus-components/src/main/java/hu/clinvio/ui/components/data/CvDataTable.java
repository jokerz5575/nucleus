package hu.clinvio.ui.components.data;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clinvio Data Table component.
 * Maps to Bootstrap 5 table with Clinvio theme styling, sorting, pagination, and search.
 */
public class CvDataTable<T> extends AbstractCvComponent {

    private List<Column> columns = new ArrayList<>();
    private List<T> data = new ArrayList<>();
    private boolean searchable;
    private boolean sortable;
    private boolean pageable;
    private int pageSize = 10;
    private int currentPage = 0;
    private long totalItems;
    private String searchQuery;
    private String sortColumn;
    private SortDirection sortDirection;
    private String emptyMessage = "No data available";
    private String hxGet;
    private String hxTarget;

    public CvDataTable() {
        super();
        this.sortDirection = SortDirection.ASC;
    }

    public CvDataTable addColumn(String key, String header) {
        this.columns.add(new Column(key, header));
        return this;
    }

    public CvDataTable addColumn(String key, String header, boolean sortable) {
        this.columns.add(new Column(key, header, sortable));
        return this;
    }

    public CvDataTable addColumn(Column column) {
        this.columns.add(column);
        return this;
    }

    public CvDataTable data(List<T> data) {
        this.data = data;
        return this;
    }

    public CvDataTable searchable(boolean searchable) {
        this.searchable = searchable;
        return this;
    }

    public CvDataTable pageable(boolean pageable) {
        this.pageable = pageable;
        return this;
    }

    public CvDataTable pageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must be greater than 0");
        }
        this.pageSize = pageSize;
        return this;
    }

    // Getters and Setters
    public List<Column> getColumns() { return columns; }
    public void setColumns(List<Column> columns) { this.columns = columns; }

    public List<T> getData() { return data; }
    public void setData(List<T> data) { this.data = data; }

    public boolean isSearchable() { return searchable; }
    public void setSearchable(boolean searchable) { this.searchable = searchable; }

    public boolean isSortable() { return sortable; }
    public void setSortable(boolean sortable) { this.sortable = sortable; }

    public boolean isPageable() { return pageable; }
    public void setPageable(boolean pageable) { this.pageable = pageable; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = Math.max(0, currentPage); }

    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public String getSortColumn() { return sortColumn; }
    public void setSortColumn(String sortColumn) { this.sortColumn = sortColumn; }

    public SortDirection getSortDirection() { return sortDirection; }
    public void setSortDirection(SortDirection sortDirection) { this.sortDirection = sortDirection; }

    public String getEmptyMessage() { return emptyMessage; }
    public void setEmptyMessage(String emptyMessage) { this.emptyMessage = emptyMessage; }

    public String getHxGet() { return hxGet; }
    public void setHxGet(String hxGet) { this.hxGet = hxGet; }

    public String getHxTarget() { return hxTarget; }
    public void setHxTarget(String hxTarget) { this.hxTarget = hxTarget; }

    public int getTotalPages() {
        return pageable ? (int) Math.ceil((double) totalItems / pageSize) : 1;
    }

    public boolean isHasPrevious() {
        return currentPage > 0;
    }

    public boolean isHasNext() {
        return currentPage < getTotalPages() - 1;
    }

    @Override
    public String getComponentType() {
        return CvComponentType.DATA_TABLE.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.DATA_TABLE.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("columns", columns);
        model.put("data", data);
        model.put("searchable", searchable);
        model.put("sortable", sortable);
        model.put("pageable", pageable);
        model.put("pageSize", pageSize);
        model.put("currentPage", currentPage);
        model.put("totalItems", totalItems);
        model.put("searchQuery", searchQuery);
        model.put("sortColumn", sortColumn);
        model.put("sortDirection", sortDirection);
        model.put("emptyMessage", emptyMessage);
        model.put("hxGet", hxGet);
        model.put("hxTarget", hxTarget);

        // Calculate pagination
        int totalPages = pageable ? (int) Math.ceil((double) totalItems / pageSize) : 1;
        model.put("totalPages", totalPages);
        model.put("hasPrevious", currentPage > 0);
        model.put("hasNext", currentPage < totalPages - 1);

        return model;
    }

    public static class Column {
        private final String key;
        private final String header;
        private boolean sortable;
        private boolean searchable;
        private String width;
        private String align;
        private String template;

        public Column(String key, String header) {
            this.key = key;
            this.header = header;
            this.sortable = false;
        }

        public Column(String key, String header, boolean sortable) {
            this.key = key;
            this.header = header;
            this.sortable = sortable;
        }

        public Column width(String width) {
            this.width = width;
            return this;
        }

        public Column align(String align) {
            this.align = align;
            return this;
        }

        public Column template(String template) {
            this.template = template;
            return this;
        }

        // Getters
        public String getKey() { return key; }
        public String getHeader() { return header; }
        public boolean isSortable() { return sortable; }
        public boolean isSearchable() { return searchable; }
        public String getWidth() { return width; }
        public String getAlign() { return align; }
        public String getTemplate() { return template; }
    }

    public enum SortDirection {
        ASC("asc"),
        DESC("desc");

        private final String value;

        SortDirection(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
