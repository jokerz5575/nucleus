package hu.clinvio.ui.business.notification;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Notification entity for in-app notifications.
 *
 * <pre>{@code
 * CvNotification notification = CvNotification.builder()
 *     .title("Order Shipped")
 *     .message("Order ORD-001 has been shipped")
 *     .type(NotificationType.SUCCESS)
 *     .recipientId(userId)
 *     .link("/orders/1")
 *     .build();
 * }</pre>
 */
public class CvNotification {

    private String id;
    private String title;
    private String message;
    private NotificationType type;
    private String recipientId;
    private String link;
    private boolean read;
    private LocalDateTime createdAt;
    private Map<String, Object> metadata;

    public CvNotification() {}

    private CvNotification(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.message = builder.message;
        this.type = builder.type;
        this.recipientId = builder.recipientId;
        this.link = builder.link;
        this.read = false;
        this.createdAt = LocalDateTime.now();
        this.metadata = builder.metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public enum NotificationType {
        INFO("Info", "bi-info-circle", "cv-badge-neutral"),
        SUCCESS("Success", "bi-check-circle", "cv-badge-success"),
        WARNING("Warning", "bi-exclamation-triangle", "cv-badge-warning"),
        ERROR("Error", "bi-x-circle", "cv-badge-danger");

        private final String label;
        private final String icon;
        private final String badgeClass;

        NotificationType(String label, String icon, String badgeClass) {
            this.label = label;
            this.icon = icon;
            this.badgeClass = badgeClass;
        }

        public String getLabel() { return label; }
        public String getIcon() { return icon; }
        public String getBadgeClass() { return badgeClass; }
    }

    public static class Builder {
        private String id = java.util.UUID.randomUUID().toString();
        private String title;
        private String message;
        private NotificationType type = NotificationType.INFO;
        private String recipientId;
        private String link;
        private Map<String, Object> metadata;

        public Builder id(String id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder type(NotificationType type) { this.type = type; return this; }
        public Builder recipientId(String recipientId) { this.recipientId = recipientId; return this; }
        public Builder link(String link) { this.link = link; return this; }
        public Builder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }

        public CvNotification build() {
            return new CvNotification(this);
        }
    }
}
