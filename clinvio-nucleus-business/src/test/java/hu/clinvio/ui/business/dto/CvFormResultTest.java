package hu.clinvio.ui.business.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvFormResultTest {

    @Test
    void okCreatesSuccessResultWithMessage() {
        CvFormResult result = CvFormResult.ok("Success");
        assertTrue(result.isSuccess());
        assertEquals("Success", result.getMessage());
    }

    @Test
    void errorCreatesErrorResult() {
        CvFormResult result = CvFormResult.error("Something went wrong");
        assertFalse(result.isSuccess());
        assertEquals("Something went wrong", result.getMessage());
    }

    @Test
    void okWithDataStoresData() {
        Integer data = 42;
        CvFormResult result = CvFormResult.ok("Done", data);
        assertTrue(result.isSuccess());
        assertEquals(42, result.getData());
    }

    @Test
    void okWithRedirectStoresRedirectUrl() {
        CvFormResult result = CvFormResult.ok("Redirecting", "/dashboard");
        assertTrue(result.isSuccess());
        assertEquals("/dashboard", result.getRedirectUrl());
    }

    @Test
    void isSuccessReturnsCorrectValue() {
        assertTrue(CvFormResult.ok("ok").isSuccess());
        assertFalse(CvFormResult.error("err").isSuccess());
    }

    @Test
    void getMessageReturnsTheSetMessage() {
        CvFormResult result = new CvFormResult();
        result.setMessage("custom message");
        assertEquals("custom message", result.getMessage());
    }
}
