package hu.clinvio.ui.business.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CvPageResultTest {

    @Test
    void constructorStoresPageSizeTotal() {
        List<String> content = List.of("a", "b", "c");
        CvPageResult<String> result = new CvPageResult<>(content, 25, 3, 0, 10);
        assertEquals(content, result.getContent());
        assertEquals(25, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
        assertEquals(0, result.getCurrentPage());
        assertEquals(10, result.getPageSize());
    }

    @Test
    void emptyPageResult() {
        CvPageResult<String> result = CvPageResult.empty(10);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getCurrentPage());
    }

    @Test
    void hasNextReturnsTrueWhenMorePagesExist() {
        CvPageResult<String> result = new CvPageResult<>(List.of("a"), 25, 3, 0, 10);
        assertTrue(result.isHasNext());
    }

    @Test
    void hasNextReturnsFalseOnLastPage() {
        CvPageResult<String> result = new CvPageResult<>(List.of("a"), 25, 3, 2, 10);
        assertFalse(result.isHasNext());
    }

    @Test
    void hasPreviousReturnsTrueWhenNotOnFirstPage() {
        CvPageResult<String> result = new CvPageResult<>(List.of("a"), 25, 3, 1, 10);
        assertTrue(result.isHasPrevious());
    }

    @Test
    void hasPreviousReturnsFalseOnFirstPage() {
        CvPageResult<String> result = new CvPageResult<>(List.of("a"), 25, 3, 0, 10);
        assertFalse(result.isHasPrevious());
    }
}
