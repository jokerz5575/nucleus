package hu.clinvio.ui.business.service;

import hu.clinvio.ui.business.repository.CvBaseRepository;
import hu.clinvio.ui.persistence.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Extended base service with search and specification support.
 *
 * <p>Use this when your entity needs text search or filtered queries:</p>
 * <pre>{@code
 * @Service
 * public class CustomerService extends CvSearchableService<Customer, Long> {
 *     public CustomerService(CustomerRepository repo) {
 *         super(repo, repo);
 *     }
 *
 *     @Override
 *     public Page<Customer> search(String query, Pageable pageable) {
 *         return ((CustomerRepository) repository).search(query, pageable);
 *     }
 * }
 * }</pre>
 *
 * @param <E>  the entity type
 * @param <ID> the primary key type
 */
public abstract class CvSearchableService<E extends BaseEntity, ID> extends CvBaseService<E, ID> {

    protected final JpaSpecificationExecutor<E> specExecutor;

    protected CvSearchableService(CvBaseRepository<E, ID> repository,
                                   JpaSpecificationExecutor<E> specExecutor) {
        super(repository);
        this.specExecutor = specExecutor;
    }

    /**
     * Search entities by a text query. Must be implemented by subclasses.
     *
     * @param query    the search text
     * @param pageable pagination info
     * @return matching entities
     */
    public abstract Page<E> search(String query, Pageable pageable);

    /**
     * Find entities matching a JPA Specification.
     *
     * @param spec     the specification to match
     * @param pageable pagination info
     * @return matching entities
     */
    public Page<E> findBySpec(Specification<E> spec, Pageable pageable) {
        return specExecutor.findAll(spec, pageable);
    }

    /**
     * Find entities matching a specification with default pagination.
     */
    public Page<E> findBySpec(Specification<E> spec) {
        return specExecutor.findAll(spec, Pageable.unpaged());
    }

    /**
     * Count entities matching a specification.
     */
    public long countBySpec(Specification<E> spec) {
        return specExecutor.count(spec);
    }
}
