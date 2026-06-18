package hu.clinvio.ui.business.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utility class for pagination helpers.
 *
 * <p>Use to create Pageable objects with common patterns:</p>
 * <pre>{@code
 * // Default pagination (page 0, 10 items)
 * Pageable pageable = Pagination.of();
 *
 * // Custom page and size
 * Pageable pageable = Pagination.of(2, 25);
 *
 * // With sorting
 * Pageable pageable = Pagination.of(0, 10, "createdAt", Sort.Direction.DESC);
 *
 * // From a Page result, get next/previous page
 * Pageable next = Pagination.nextPage(page);
 * Pageable prev = Pagination.previousPage(page);
 * }</pre>
 */
public class Pagination {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    private Pagination() {}

    /**
     * Create a default Pageable (page 0, size 10).
     */
    public static Pageable of() {
        return PageRequest.of(0, DEFAULT_PAGE_SIZE);
    }

    /**
     * Create a Pageable with specified page and size.
     */
    public static Pageable of(int page, int size) {
        return PageRequest.of(Math.max(0, page), Math.min(size, MAX_PAGE_SIZE));
    }

    /**
     * Create a Pageable with sorting.
     */
    public static Pageable of(int page, int size, String sortField, Sort.Direction direction) {
        return PageRequest.of(Math.max(0, page), Math.min(size, MAX_PAGE_SIZE),
                Sort.by(direction, sortField));
    }

    /**
     * Create a Pageable with multiple sort fields.
     */
    public static Pageable of(int page, int size, Sort sort) {
        return PageRequest.of(Math.max(0, page), Math.min(size, MAX_PAGE_SIZE), sort);
    }

    /**
     * Get the next page Pageable from a current Page.
     */
    public static Pageable nextPage(Page<?> currentPage) {
        if (currentPage.hasNext()) {
            return currentPage.nextPageable();
        }
        return currentPage.getPageable();
    }

    /**
     * Get the previous page Pageable from a current Page.
     */
    public static Pageable previousPage(Page<?> currentPage) {
        if (currentPage.hasPrevious()) {
            return currentPage.previousPageable();
        }
        return currentPage.getPageable();
    }

    /**
     * Create a Pageable for the first page with a specific size.
     */
    public static Pageable firstPage(int size) {
        return PageRequest.of(0, Math.min(size, MAX_PAGE_SIZE));
    }

    /**
     * Create a Pageable for the last page.
     */
    public static Pageable lastPage(long totalElements, int size) {
        int lastPage = (int) Math.ceil((double) totalElements / size) - 1;
        return PageRequest.of(Math.max(0, lastPage), Math.min(size, MAX_PAGE_SIZE));
    }

    /**
     * Calculate total pages.
     */
    public static int totalPages(long totalElements, int pageSize) {
        return (int) Math.ceil((double) totalElements / Math.max(1, pageSize));
    }

    /**
     * Validate and normalize page parameters.
     */
    public static Pageable normalize(int page, int size, String sortField, Sort.Direction direction) {
        int normalizedPage = Math.max(0, page);
        int normalizedSize = Math.max(1, Math.min(size, MAX_PAGE_SIZE));

        if (sortField != null && !sortField.isBlank()) {
            return PageRequest.of(normalizedPage, normalizedSize,
                    Sort.by(direction != null ? direction : Sort.Direction.ASC, sortField));
        }
        return PageRequest.of(normalizedPage, normalizedSize);
    }
}
