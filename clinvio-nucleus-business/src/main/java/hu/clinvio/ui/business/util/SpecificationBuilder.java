package hu.clinvio.ui.business.util;

import hu.clinvio.ui.persistence.entity.BaseEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building JPA Specifications dynamically.
 *
 * <p>Use this to build complex queries with multiple filters:</p>
 * <pre>{@code
 * Specification<Order> spec = SpecificationBuilder.<Order>create()
 *     .equal("status", OrderStatus.ACTIVE)
 *     .like("customerName", searchQuery)
 *     .between("createdAt", startDate, endDate)
 *     .greaterThan("totalAmount", minAmount)
 *     .build();
 *
 * Page<Order> results = orderRepository.findAll(spec, pageable);
 * }</pre>
 *
 * @param <E> the entity type
 */
public class SpecificationBuilder<E extends BaseEntity> {

    private final List<Specification<E>> specifications = new ArrayList<>();

    private SpecificationBuilder() {}

    /**
     * Create a new SpecificationBuilder.
     */
    public static <T extends BaseEntity> SpecificationBuilder<T> create() {
        return new SpecificationBuilder<>();
    }

    /**
     * Add an equal filter.
     *
     * @param field the field name
     * @param value the value to match
     * @return this builder
     */
    public SpecificationBuilder<E> equal(String field, Object value) {
        if (value != null) {
            specifications.add((root, query, cb) -> cb.equal(root.get(field), value));
        }
        return this;
    }

    /**
     * Add a not-equal filter.
     */
    public SpecificationBuilder<E> notEqual(String field, Object value) {
        if (value != null) {
            specifications.add((root, query, cb) -> cb.notEqual(root.get(field), value));
        }
        return this;
    }

    /**
     * Add a LIKE filter (case-insensitive).
     *
     * @param field the field name
     * @param value the value to search for (wraps with %)
     * @return this builder
     */
    public SpecificationBuilder<E> like(String field, String value) {
        if (value != null && !value.isBlank()) {
            specifications.add((root, query, cb) ->
                    cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%"));
        }
        return this;
    }

    /**
     * Add a starts-with filter (case-insensitive).
     */
    public SpecificationBuilder<E> startsWith(String field, String value) {
        if (value != null && !value.isBlank()) {
            specifications.add((root, query, cb) ->
                    cb.like(cb.lower(root.get(field)), value.toLowerCase() + "%"));
        }
        return this;
    }

    /**
     * Add a between filter for comparable values.
     *
     * @param field the field name
     * @param from  the minimum value (inclusive)
     * @param to    the maximum value (inclusive)
     * @return this builder
     */
    public <T extends Comparable<T>> SpecificationBuilder<E> between(String field, T from, T to) {
        if (from != null && to != null) {
            specifications.add((root, query, cb) -> cb.between(root.get(field), from, to));
        } else if (from != null) {
            specifications.add((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field), from));
        } else if (to != null) {
            specifications.add((root, query, cb) -> cb.lessThanOrEqualTo(root.get(field), to));
        }
        return this;
    }

    /**
     * Add a greater-than filter.
     */
    public <T extends Comparable<T>> SpecificationBuilder<E> greaterThan(String field, T value) {
        if (value != null) {
            specifications.add((root, query, cb) -> cb.greaterThan(root.get(field), value));
        }
        return this;
    }

    /**
     * Add a greater-than-or-equal filter.
     */
    public <T extends Comparable<T>> SpecificationBuilder<E> greaterThanOrEqual(String field, T value) {
        if (value != null) {
            specifications.add((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field), value));
        }
        return this;
    }

    /**
     * Add a less-than filter.
     */
    public <T extends Comparable<T>> SpecificationBuilder<E> lessThan(String field, T value) {
        if (value != null) {
            specifications.add((root, query, cb) -> cb.lessThan(root.get(field), value));
        }
        return this;
    }

    /**
     * Add a less-than-or-equal filter.
     */
    public <T extends Comparable<T>> SpecificationBuilder<E> lessThanOrEqual(String field, T value) {
        if (value != null) {
            specifications.add((root, query, cb) -> cb.lessThanOrEqualTo(root.get(field), value));
        }
        return this;
    }

    /**
     * Add an IN filter.
     */
    public SpecificationBuilder<E> in(String field, List<?> values) {
        if (values != null && !values.isEmpty()) {
            specifications.add((root, query, cb) -> root.get(field).in(values));
        }
        return this;
    }

    /**
     * Add a NOT IN filter.
     */
    public SpecificationBuilder<E> notIn(String field, List<?> values) {
        if (values != null && !values.isEmpty()) {
            specifications.add((root, query, cb) -> cb.not(root.get(field).in(values)));
        }
        return this;
    }

    /**
     * Add a null check.
     */
    public SpecificationBuilder<E> isNull(String field) {
        specifications.add((root, query, cb) -> cb.isNull(root.get(field)));
        return this;
    }

    /**
     * Add a not-null check.
     */
    public SpecificationBuilder<E> isNotNull(String field) {
        specifications.add((root, query, cb) -> cb.isNotNull(root.get(field)));
        return this;
    }

    /**
     * Add a date range filter.
     */
    public SpecificationBuilder<E> dateBetween(String field, LocalDateTime from, LocalDateTime to) {
        return between(field, from, to);
    }

    /**
     * Add a custom specification.
     */
    public SpecificationBuilder<E> custom(Specification<E> spec) {
        if (spec != null) {
            specifications.add(spec);
        }
        return this;
    }

    /**
     * Add a soft delete filter (excludes deleted entities).
     */
    public SpecificationBuilder<E> active() {
        specifications.add((root, query, cb) -> cb.isNull(root.get("deletedAt")));
        return this;
    }

    /**
     * Build the final Specification by combining all filters with AND.
     *
     * @return the combined Specification, or null if no filters
     */
    public Specification<E> build() {
        if (specifications.isEmpty()) {
            return null;
        }

        Specification<E> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = result.and(specifications.get(i));
        }
        return result;
    }

    /**
     * Build the final Specification by combining all filters with OR.
     *
     * @return the combined Specification, or null if no filters
     */
    public Specification<E> buildOr() {
        if (specifications.isEmpty()) {
            return null;
        }

        Specification<E> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = result.or(specifications.get(i));
        }
        return result;
    }
}
