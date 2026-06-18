package hu.clinvio.ui.business.repository;

import hu.clinvio.ui.persistence.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Base repository interface for all Clinvio entities.
 * Provides soft delete support and common query methods.
 *
 * <p>Extend this interface in your application repositories:</p>
 * <pre>{@code
 * public interface CustomerRepository extends CvBaseRepository<Customer, Long> {
 *     // Add custom queries here
 * }
 * }</pre>
 *
 * @param <E>  the entity type
 * @param <ID> the primary key type
 */
@NoRepositoryBean
public interface CvBaseRepository<E, ID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

    /**
     * Find all non-deleted entities.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL")
    List<E> findAllActive();

    /**
     * Find all non-deleted entities with sorting.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL")
    List<E> findAllActive(Sort sort);

    /**
     * Find all non-deleted entities with pagination.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL")
    Page<E> findAllActive(Pageable pageable);

    /**
     * Find a non-deleted entity by ID.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.deletedAt IS NULL")
    Optional<E> findActiveById(@Param("id") ID id);

    /**
     * Count non-deleted entities.
     */
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.deletedAt IS NULL")
    long countActive();

    /**
     * Soft delete an entity by setting deletedAt timestamp.
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
    void softDelete(@Param("id") ID id);

    /**
     * Check if a non-deleted entity exists by ID.
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM #{#entityName} e WHERE e.id = :id AND e.deletedAt IS NULL")
    boolean existsActiveById(@Param("id") ID id);

    /**
     * Find entities created after a specific date.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL AND e.createdAt >= :since")
    List<E> findCreatedSince(@Param("since") LocalDateTime since);

    /**
     * Count entities created after a specific date.
     */
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.deletedAt IS NULL AND e.createdAt >= :since")
    long countCreatedSince(@Param("since") LocalDateTime since);

    /**
     * Find entities updated after a specific date.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL AND e.updatedAt >= :since")
    List<E> findUpdatedSince(@Param("since") LocalDateTime since);

    /**
     * Find the most recent entities.
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL ORDER BY e.createdAt DESC")
    List<E> findRecent(Pageable pageable);

    /**
     * Search entities by a text query. Override in concrete repositories
     * to define search behavior.
     *
     * @param query    the search text
     * @param pageable pagination info
     * @return matching entities
     */
    default Page<E> search(String query, Pageable pageable) {
        // Default: return all active entities (override for custom search)
        return findAllActive(pageable);
    }
}
