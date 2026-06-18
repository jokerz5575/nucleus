package hu.clinvio.ui.business.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvSearchRequestTest {

    @Test
    void constructorSetsQueryPageSize() {
        CvSearchRequest request = new CvSearchRequest("test", 1, 20);
        assertEquals("test", request.getQuery());
        assertEquals(1, request.getPage());
        assertEquals(20, request.getSize());
    }

    @Test
    void defaultValuesExist() {
        CvSearchRequest request = new CvSearchRequest();
        assertEquals(0, request.getPage());
        assertEquals(10, request.getSize());
    }

    @Test
    void getPageReturnsCorrectValue() {
        CvSearchRequest request = new CvSearchRequest();
        request.setPage(3);
        assertEquals(3, request.getPage());
    }

    @Test
    void getPageSizeReturnsCorrectValue() {
        CvSearchRequest request = new CvSearchRequest();
        request.setSize(50);
        assertEquals(50, request.getSize());
    }

    @Test
    void negativePageClampsToZero() {
        CvSearchRequest request = new CvSearchRequest();
        request.setPage(-5);
        assertEquals(0, request.getPage());
    }

    @Test
    void sizeAtLeastOne() {
        CvSearchRequest request = new CvSearchRequest();
        request.setSize(0);
        assertEquals(1, request.getSize());
    }
}
