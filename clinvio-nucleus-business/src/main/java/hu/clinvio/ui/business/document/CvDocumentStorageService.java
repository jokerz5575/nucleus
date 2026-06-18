package hu.clinvio.ui.business.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Service for storing and retrieving document files.
 *
 * <pre>{@code
 * @Autowired
 * private CvDocumentStorageService storageService;
 *
 * // Store a file
 * String storagePath = storageService.store(file, "orders/123");
 *
 * // Retrieve a file
 * InputStream stream = storageService.retrieve(storagePath);
 *
 * // Delete a file
 * storageService.delete(storagePath);
 * }</pre>
 */
@Service
public class CvDocumentStorageService {

    private static final Logger log = LoggerFactory.getLogger(CvDocumentStorageService.class);

    @Value("${clinvio.documents.storage-path:./data/documents}")
    private String baseStoragePath;

    /**
     * Store a file and return the storage path.
     *
     * @param file      the uploaded file
     * @param directory the subdirectory (e.g., "orders/123")
     * @return the storage path relative to base
     */
    public String store(MultipartFile file, String directory) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID() + extension;

        Path targetDir = Paths.get(baseStoragePath, directory);
        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve(uniqueFilename);
        file.transferTo(targetFile.toFile());

        log.debug("Stored file: {} -> {}", originalFilename, targetFile);
        return directory + "/" + uniqueFilename;
    }

    /**
     * Store an input stream and return the storage path.
     */
    public String store(InputStream inputStream, String filename, String directory) throws IOException {
        String extension = getExtension(filename);
        String uniqueFilename = UUID.randomUUID() + extension;

        Path targetDir = Paths.get(baseStoragePath, directory);
        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve(uniqueFilename);
        try (inputStream) {
            Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }

        log.debug("Stored file: {} -> {}", filename, targetFile);
        return directory + "/" + uniqueFilename;
    }

    /**
     * Retrieve a file as an input stream.
     */
    public InputStream retrieve(String storagePath) throws IOException {
        Path filePath = Paths.get(baseStoragePath, storagePath);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + storagePath);
        }
        return Files.newInputStream(filePath);
    }

    /**
     * Get the full path to a stored file.
     */
    public Path getPath(String storagePath) {
        return Paths.get(baseStoragePath, storagePath);
    }

    /**
     * Delete a stored file.
     */
    public boolean delete(String storagePath) {
        try {
            Path filePath = Paths.get(baseStoragePath, storagePath);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", storagePath, e);
            return false;
        }
    }

    /**
     * Check if a file exists.
     */
    public boolean exists(String storagePath) {
        return Files.exists(Paths.get(baseStoragePath, storagePath));
    }

    /**
     * Get file size in bytes.
     */
    public long getFileSize(String storagePath) throws IOException {
        return Files.size(Paths.get(baseStoragePath, storagePath));
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return lastDot >= 0 ? filename.substring(lastDot) : "";
    }
}
