package hu.clinvio.ui.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Base entity class providing common audit fields for all JPA entities.
 *
 * <p>Entities extending this class automatically get:</p>
 * <ul>
 *   <li>{@code id} - Auto-generated primary key</li>
 *   <li>{@code createdAt} - Timestamp set on first persist</li>
 *   <li>{@code updatedAt} - Timestamp set on every update</li>
 *   <li>{@code deletedAt} - Timestamp for soft delete (null when active)</li>
 * </ul>
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * @Entity
 * public class Order extends BaseEntity {
 *     private String orderNumber;
 *     // id, createdAt, updatedAt, deletedAt inherited
 * }
 * }</pre>
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * Check if this entity is soft-deleted.
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
