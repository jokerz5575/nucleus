package hu.clinvio.ui.business.document;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CvDocumentStorageServiceTest {

    private CvDocumentStorageService storageService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        storageService = new CvDocumentStorageService();
        ReflectionTestUtils.setField(storageService, "baseStoragePath", tempDir.toString());
    }

    @Test
    void storeDocumentCreatesFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "Hello World".getBytes(StandardCharsets.UTF_8));

        String storagePath = storageService.store(file, "test-dir");
        assertNotNull(storagePath);
        assertTrue(storagePath.startsWith("test-dir/"));
    }

    @Test
    void retrieveReturnsBytes() throws Exception {
        byte[] content = "File content".getBytes(StandardCharsets.UTF_8);
        String storagePath = storageService.store(
                new ByteArrayInputStream(content), "test.txt", "retrieve-dir");

        InputStream retrieved = storageService.retrieve(storagePath);
        assertNotNull(retrieved);
        byte[] retrievedBytes = retrieved.readAllBytes();
        assertArrayEquals(content, retrievedBytes);
    }

    @Test
    void deleteRemovesFile() throws Exception {
        String storagePath = storageService.store(
                new ByteArrayInputStream("delete me".getBytes(StandardCharsets.UTF_8)),
                "delete.txt", "delete-dir");

        assertTrue(storageService.exists(storagePath));
        boolean deleted = storageService.delete(storagePath);
        assertTrue(deleted);
        assertFalse(storageService.exists(storagePath));
    }

    @Test
    void nullInputThrowsException() {
        assertThrows(Exception.class, () -> storageService.store(
                (InputStream) null, "test.txt", "dir"));
    }
}
