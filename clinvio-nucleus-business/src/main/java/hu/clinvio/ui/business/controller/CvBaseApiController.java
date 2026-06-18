package hu.clinvio.ui.business.controller;

import hu.clinvio.ui.business.dto.CvFormResult;
import hu.clinvio.ui.business.dto.CvPageResult;
import hu.clinvio.ui.business.exception.CvNotFoundException;
import hu.clinvio.ui.business.service.CvBaseService;
import hu.clinvio.ui.persistence.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Abstract base controller for REST API endpoints.
 *
 * <pre>{@code
 * @RestController
 * @RequestMapping("/api/customers")
 * public class CustomerApiController extends CvBaseApiController<Customer> {
 *     public CustomerApiController(CustomerService service) {
 *         super(service);
 *     }
 * }
 * }</pre>
 *
 * @param <E> the entity type
 */
public abstract class CvBaseApiController<E extends BaseEntity> {

    protected final CvBaseService<E, Long> service;

    protected CvBaseApiController(CvBaseService<E, Long> service) {
        this.service = service;
    }

    /**
     * GET /api/entities - List all entities with pagination.
     */
    @GetMapping
    public ResponseEntity<CvPageResult<E>> list(Pageable pageable) {
        Page<E> page = service.findAll(pageable);
        return ResponseEntity.ok(CvPageResult.from(page));
    }

    /**
     * GET /api/entities/{id} - Get entity by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<E> getById(@PathVariable Long id) {
        E entity = service.getById(id);
        return ResponseEntity.ok(entity);
    }

    /**
     * POST /api/entities - Create a new entity.
     */
    @PostMapping
    public ResponseEntity<CvFormResult> create(@RequestBody E entity) {
        E created = service.create(entity);
        CvFormResult result = CvFormResult.ok(getEntityName() + " created");
        result.setRedirectUrl(getEntityPath() + "/" + created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * PUT /api/entities/{id} - Update an existing entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CvFormResult> update(@PathVariable Long id, @RequestBody E entity) {
        entity.setId(id);
        E updated = service.update(entity);
        return ResponseEntity.ok(CvFormResult.ok(getEntityName() + " updated"));
    }

    /**
     * DELETE /api/entities/{id} - Soft delete an entity.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CvFormResult> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(CvFormResult.ok(getEntityName() + " deleted"));
    }

    /**
     * GET /api/entities/all - Get all entities without pagination.
     */
    @GetMapping("/all")
    public ResponseEntity<List<E>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * GET /api/entities/count - Count all entities.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    /**
     * GET /api/entities/{id}/exists - Check if entity exists.
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    protected abstract String getEntityName();
    protected abstract String getEntityPath();
}
