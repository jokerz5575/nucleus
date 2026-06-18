package hu.clinvio.ui.business.service;

import hu.clinvio.ui.business.exception.CvBusinessException;
import hu.clinvio.ui.business.exception.CvNotFoundException;
import hu.clinvio.ui.business.repository.CvBaseRepository;
import hu.clinvio.ui.persistence.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base service class providing generic CRUD operations.
 *
 * <p>Extend this in your application services:</p>
 * <pre>{@code
 * @Service
 * public class CustomerService extends CvBaseService<Customer, Long> {
 *     public CustomerService(CustomerRepository repo) {
 *         super(repo);
 *     }
 *     // Add custom business methods here
 * }
 * }</pre>
 *
 * @param <E>  the entity type
 * @param <ID> the primary key type
 */
public abstract class CvBaseService<E extends BaseEntity, ID> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final CvBaseRepository<E, ID> repository;

    protected CvBaseService(CvBaseRepository<E, ID> repository) {
        this.repository = repository;
    }

    // ==================== Query Methods ====================

    /**
     * Find all non-deleted entities with pagination.
     */
    public Page<E> findAll(Pageable pageable) {
        return repository.findAllActive(pageable);
    }

    /**
     * Find all non-deleted entities.
     */
    public List<E> findAll() {
        return repository.findAllActive();
    }

    /**
     * Find all non-deleted entities with sorting.
     */
    public List<E> findAll(Sort sort) {
        return repository.findAllActive(sort);
    }

    /**
     * Find a non-deleted entity by ID.
     */
    public Optional<E> findById(ID id) {
        return repository.findActiveById(id);
    }

    /**
     * Find an entity by ID or throw CvNotFoundException.
     */
    public E getById(ID id) {
        return repository.findActiveById(id)
                .orElseThrow(() -> new CvNotFoundException(getEntityName(), id));
    }

    /**
     * Find multiple entities by their IDs.
     */
    public List<E> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids).stream()
                .filter(e -> !e.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Check if an entity exists by ID (and is not deleted).
     */
    public boolean existsById(ID id) {
        return repository.findActiveById(id).isPresent();
    }

    /**
     * Count non-deleted entities.
     */
    public long count() {
        return repository.countActive();
    }

    // ==================== Create Methods ====================

    /**
     * Create a new entity.
     */
    @Transactional
    public E create(E entity) {
        validateBeforeCreate(entity);
        E saved = repository.save(entity);
        log.debug("Created {} with id: {}", getEntityName(), saved.getId());
        return saved;
    }

    /**
     * Create multiple entities in a batch.
     */
    @Transactional
    public List<E> createAll(List<E> entities) {
        entities.forEach(this::validateBeforeCreate);
        List<E> saved = repository.saveAll(entities);
        log.debug("Created {} {}s", saved.size(), getEntityName());
        return saved;
    }

    // ==================== Update Methods ====================

    /**
     * Update an existing entity.
     */
    @Transactional
    public E update(E entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Cannot update entity without id");
        }
        validateBeforeUpdate(entity);
        E saved = repository.save(entity);
        log.debug("Updated {} with id: {}", getEntityName(), saved.getId());
        return saved;
    }

    /**
     * Update multiple entities in a batch.
     */
    @Transactional
    public List<E> updateAll(List<E> entities) {
        entities.forEach(e -> {
            if (e.getId() == null) {
                throw new IllegalArgumentException("Cannot update entity without id");
            }
            validateBeforeUpdate(e);
        });
        List<E> saved = repository.saveAll(entities);
        log.debug("Updated {} {}s", saved.size(), getEntityName());
        return saved;
    }

    // ==================== Delete Methods ====================

    /**
     * Soft delete an entity by ID.
     */
    @Transactional
    public void delete(ID id) {
        E entity = repository.findActiveById(id)
                .orElseThrow(() -> new CvNotFoundException(getEntityName(), id));
        validateBeforeDelete(entity);
        repository.softDelete(id);
        log.debug("Soft deleted {} with id: {}", getEntityName(), id);
    }

    /**
     * Soft delete multiple entities by IDs.
     */
    @Transactional
    public void deleteAll(List<ID> ids) {
        ids.forEach(id -> {
            E entity = repository.findActiveById(id)
                    .orElseThrow(() -> new CvNotFoundException(getEntityName(), id));
            validateBeforeDelete(entity);
            repository.softDelete(id);
        });
        log.debug("Soft deleted {} {}s", ids.size(), getEntityName());
    }

    /**
     * Check if an entity can be deleted. Override to add custom logic.
     */
    protected boolean canDelete(E entity) {
        return true;
    }

    // ==================== Pagination Helpers ====================

    /**
     * Find with default page size of 10.
     */
    public Page<E> findPage(int page) {
        return findAll(Pageable.ofSize(10).withPage(page));
    }

    /**
     * Find with custom page size.
     */
    public Page<E> findPage(int page, int size) {
        return findAll(Pageable.ofSize(size).withPage(page));
    }

    /**
     * Find first N entities.
     */
    public List<E> findTopN(int limit) {
        return repository.findAll(Pageable.ofSize(limit)).getContent();
    }

    /**
     * Find most recent entities.
     */
    public List<E> findRecent(int limit) {
        return repository.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).getContent();
    }

    // ==================== Validation Hooks ====================

    /**
     * Validate entity before creation. Override to add custom validation.
     *
     * @param entity the entity to validate
     * @throws CvBusinessException if validation fails
     */
    protected void validateBeforeCreate(E entity) {
        // Default: no validation. Override in subclass.
    }

    /**
     * Validate entity before update. Override to add custom validation.
     *
     * @param entity the entity to validate
     * @throws CvBusinessException if validation fails
     */
    protected void validateBeforeUpdate(E entity) {
        // Default: no validation. Override in subclass.
    }

    /**
     * Validate entity before deletion. Override to add custom validation.
     *
     * @param entity the entity to validate
     * @throws CvBusinessException if validation fails
     */
    protected void validateBeforeDelete(E entity) {
        if (!canDelete(entity)) {
            throw new CvBusinessException(getEntityName() + " cannot be deleted");
        }
    }

    // ==================== Utility Methods ====================

    /**
     * Get the entity name for error messages. Override to customize.
     */
    protected String getEntityName() {
        return "Entity";
    }

    /**
     * Execute a function with entity lookup. Throws if not found.
     */
    @Transactional
    public E updateField(ID id, java.util.function.Consumer<E> updater) {
        E entity = getById(id);
        updater.accept(entity);
        return repository.save(entity);
    }

    /**
     * Restore a soft-deleted entity.
     */
    @Transactional
    public E restore(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new CvNotFoundException(getEntityName(), id));
        entity.setDeletedAt(null);
        E restored = repository.save(entity);
        log.debug("Restored {} with id: {}", getEntityName(), id);
        return restored;
    }

    /**
     * Count entities created after a specific date.
     */
    public long countCreatedAfter(LocalDateTime date) {
        return repository.findAll().stream()
                .filter(e -> e.getCreatedAt() != null && e.getCreatedAt().isAfter(date))
                .count();
    }
}
