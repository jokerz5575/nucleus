package hu.clinvio.ui.business.filter;

import java.util.*;

public class CvFilterGroup {
    private final List<CvFilter> filters = new ArrayList<>();
    private LogicalOperator operator = LogicalOperator.AND;

    public static CvFilterGroup create() { return new CvFilterGroup(); }

    public CvFilterGroup equal(String field, Object value) {
        filters.add(new CvFilter(field, FilterOperator.EQUALS, value));
        return this;
    }

    public CvFilterGroup notEqual(String field, Object value) {
        filters.add(new CvFilter(field, FilterOperator.NOT_EQUALS, value));
        return this;
    }

    public CvFilterGroup contains(String field, String value) {
        filters.add(new CvFilter(field, FilterOperator.CONTAINS, value));
        return this;
    }

    public CvFilterGroup greaterThan(String field, Object value) {
        filters.add(new CvFilter(field, FilterOperator.GREATER_THAN, value));
        return this;
    }

    public CvFilterGroup lessThan(String field, Object value) {
        filters.add(new CvFilter(field, FilterOperator.LESS_THAN, value));
        return this;
    }

    public CvFilterGroup between(String field, Object from, Object to) {
        filters.add(new CvFilter(field, FilterOperator.BETWEEN, new Object[]{from, to}));
        return this;
    }

    public CvFilterGroup in(String field, List<?> values) {
        filters.add(new CvFilter(field, FilterOperator.IN, values));
        return this;
    }

    public CvFilterGroup isNull(String field) {
        filters.add(new CvFilter(field, FilterOperator.IS_NULL, null));
        return this;
    }

    public CvFilterGroup isNotNull(String field) {
        filters.add(new CvFilter(field, FilterOperator.IS_NOT_NULL, null));
        return this;
    }

    public CvFilterGroup operator(LogicalOperator operator) {
        this.operator = operator;
        return this;
    }

    public List<CvFilter> getFilters() { return Collections.unmodifiableList(filters); }
    public LogicalOperator getOperator() { return operator; }
    public boolean isEmpty() { return filters.isEmpty(); }

    public enum LogicalOperator { AND, OR }
    public enum FilterOperator { EQUALS, NOT_EQUALS, CONTAINS, NOT_CONTAINS, STARTS_WITH, ENDS_WITH, GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL, BETWEEN, IN, NOT_IN, IS_NULL, IS_NOT_NULL }

    public record CvFilter(String field, FilterOperator operator, Object value) {}
}
