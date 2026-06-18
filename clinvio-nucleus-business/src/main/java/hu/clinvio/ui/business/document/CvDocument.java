package hu.clinvio.ui.business.document;

import hu.clinvio.ui.persistence.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a document attachment.
 * Store metadata about uploaded files linked to other entities.
 *
 * <pre>{@code
 * @Entity
 * public class OrderDocument extends CvDocument {
 *     @ManyToOne
 *     private Order order;
 * }
 * }</pre>
 */
@MappedSuperclass
public abstract class CvDocument extends BaseEntity {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String storagePath;

    private String description;

    @Column(nullable = false)
    private String uploadedBy;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (documentType == null) {
            documentType = detectDocumentType(contentType);
        }
    }

    private DocumentType detectDocumentType(String contentType) {
        if (contentType == null) return DocumentType.OTHER;
        if (contentType.startsWith("image/")) return DocumentType.IMAGE;
        if (contentType.equals("application/pdf")) return DocumentType.PDF;
        if (contentType.startsWith("text/")) return DocumentType.TEXT;
        if (contentType.contains("spreadsheet") || contentType.contains("excel")) return DocumentType.SPREADSHEET;
        if (contentType.contains("document") || contentType.contains("word")) return DocumentType.DOCUMENT;
        return DocumentType.OTHER;
    }

    // Getters and Setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }

    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }

    /**
     * Get human-readable file size.
     */
    public String getFormattedFileSize() {
        if (fileSize == null) return "Unknown";
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        if (fileSize < 1024 * 1024 * 1024) return String.format("%.1f MB", fileSize / (1024.0 * 1024));
        return String.format("%.1f GB", fileSize / (1024.0 * 1024 * 1024));
    }

    /**
     * Document type enum.
     */
    public enum DocumentType {
        PDF("PDF", "bi-file-earmark-pdf", "cv-badge-danger"),
        IMAGE("Image", "bi-image", "cv-badge-info"),
        DOCUMENT("Document", "bi-file-earmark-word", "cv-badge-primary"),
        SPREADSHEET("Spreadsheet", "bi-file-earmark-excel", "cv-badge-success"),
        TEXT("Text", "bi-file-earmark-text", "cv-badge-neutral"),
        OTHER("Other", "bi-file-earmark", "cv-badge-neutral");

        private final String label;
        private final String icon;
        private final String badgeClass;

        DocumentType(String label, String icon, String badgeClass) {
            this.label = label;
            this.icon = icon;
            this.badgeClass = badgeClass;
        }

        public String getLabel() { return label; }
        public String getIcon() { return icon; }
        public String getBadgeClass() { return badgeClass; }
    }
}
