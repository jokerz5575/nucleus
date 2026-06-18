package hu.clinvio.ui.business.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Standardized paginated result wrapper.
 * Compatible with CvDataTable component.
 *
 * @param <T> the element type
 */
public class CvPageResult<T> {

    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
    private final int pageSize;
    private final boolean hasNext;
    private final boolean hasPrevious;

    public CvPageResult(List<T> content, long totalElements, int totalPages,
                        int currentPage, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.hasNext = currentPage < totalPages - 1;
        this.hasPrevious = currentPage > 0;
    }

    /**
     * Create from a Spring Data Page.
     */
    public static <T> CvPageResult<T> from(Page<T> page) {
        return new CvPageResult<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    /**
     * Create an empty result.
     */
    public static <T> CvPageResult<T> empty(int pageSize) {
        return new CvPageResult<>(List.of(), 0, 0, 0, pageSize);
    }

    public List<T> getContent() { return content; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
    public boolean isHasNext() { return hasNext; }
    public boolean isHasPrevious() { return hasPrevious; }
    public boolean isEmpty() { return content.isEmpty(); }
}
