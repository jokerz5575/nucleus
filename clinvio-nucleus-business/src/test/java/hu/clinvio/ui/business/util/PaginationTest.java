package hu.clinvio.ui.business.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

class PaginationTest {

    @AfterEach
    void tearDown() {
        Pagination.configure(10, 100);
    }

    @Test
    void configureSetsDefaults() {
        Pagination.configure(25, 200);
        assertEquals(25, Pagination.DEFAULT_PAGE_SIZE());
        assertEquals(200, Pagination.MAX_PAGE_SIZE());
    }

    @Test
    void pageSizeIsLimitedToMax() {
        Pageable pageable = Pagination.of(0, 500);
        assertTrue(pageable.getPageSize() <= Pagination.MAX_PAGE_SIZE());
    }

    @Test
    void defaultValuesExist() {
        assertTrue(Pagination.DEFAULT_PAGE_SIZE() > 0);
        assertTrue(Pagination.MAX_PAGE_SIZE() > 0);
    }

    @Test
    void ofReturnsPageableWithCorrectPageAndSize() {
        Pageable pageable = Pagination.of(2, 15);
        assertEquals(2, pageable.getPageNumber());
        assertEquals(15, pageable.getPageSize());
    }

    @Test
    void ofWithSortReturnsSortedPageable() {
        Pageable pageable = Pagination.of(0, 10, "name", Sort.Direction.ASC);
        assertTrue(pageable.getSort().isSorted());
    }
}
