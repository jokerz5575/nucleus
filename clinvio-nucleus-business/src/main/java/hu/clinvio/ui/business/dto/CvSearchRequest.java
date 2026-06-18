package hu.clinvio.ui.business.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

/**
 * Search + filter + sort + pagination request DTO.
 * Used to encapsulate all search parameters from the client.
 */
public class CvSearchRequest {

    private String query;
    private Map<String, Object> filters = new HashMap<>();
    private String sortField;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private int page = 0;
    private int size = 10;

    public CvSearchRequest() {}

    public CvSearchRequest(String query, int page, int size) {
        this.query = query;
        this.page = page;
        this.size = size;
    }

    /**
     * Convert to Spring Data Pageable.
     */
    public Pageable toPageable() {
        if (sortField != null && !sortField.isBlank()) {
            Sort sort = Sort.by(sortDirection, sortField);
            return PageRequest.of(page, size, sort);
        }
        return PageRequest.of(page, size);
    }

    /**
     * Check if a search query is present.
     */
    public boolean hasQuery() {
        return query != null && !query.isBlank();
    }

    /**
     * Check if any filters are present.
     */
    public boolean hasFilters() {
        return filters != null && !filters.isEmpty();
    }

    /**
     * Get a filter value by key.
     */
    public Object getFilter(String key) {
        return filters.get(key);
    }

    /**
     * Add a filter.
     */
    public CvSearchRequest filter(String key, Object value) {
        this.filters.put(key, value);
        return this;
    }

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public Map<String, Object> getFilters() { return filters; }
    public void setFilters(Map<String, Object> filters) { this.filters = filters; }

    public String getSortField() { return sortField; }
    public void setSortField(String sortField) { this.sortField = sortField; }

    public Sort.Direction getSortDirection() { return sortDirection; }
    public void setSortDirection(Sort.Direction sortDirection) { this.sortDirection = sortDirection; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = Math.max(0, page); }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = Math.max(1, size); }
}
