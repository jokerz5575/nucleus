package hu.clinvio.ui.business.controller;

import hu.clinvio.ui.business.exception.CvNotFoundException;
import hu.clinvio.ui.business.service.CvBaseService;
import hu.clinvio.ui.components.data.CvDataTable;
import hu.clinvio.ui.htmx.filter.HxRequestFilter;
import hu.clinvio.ui.htmx.response.HxResponse;
import hu.clinvio.ui.htmx.response.HxSwap;
import hu.clinvio.ui.persistence.entity.BaseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Abstract base controller providing standard CRUD endpoints.
 *
 * <p>Subclasses must add {@code @GetMapping} to their own listView method:</p>
 * <pre>{@code
 * @Controller
 * @RequestMapping("/customers")
 * public class CustomerController extends CvBaseCrudController<Customer> {
 *
 *     @GetMapping  // Required - add your own mapping
 *     public String listView(Model model, Pageable pageable) {
 *         return super.listView(model, pageable);  // or custom dashboard
 *     }
 *
 *     @GetMapping("/{id}")  // Required
 *     public String detailView(@PathVariable Long id, Model model) {
 *         return super.detailView(id, model);
 *     }
 * }
 * }</pre>
 */
public abstract class CvBaseCrudController<E extends BaseEntity> {

    protected final CvBaseService<E, Long> service;

    protected CvBaseCrudController(CvBaseService<E, Long> service) {
        this.service = service;
    }

    // ==================== Template Hooks ====================

    protected abstract String getListTemplate();
    protected abstract String getDetailTemplate();
    protected abstract String getEntityName();
    protected abstract String getEntityPath();
    protected abstract CvDataTable<E> buildDataTable(Page<E> page);

    // ==================== View Methods (no annotations - subclasses add @GetMapping) ====================

    /**
     * List view with pagination. Call from subclass with @GetMapping.
     */
    public String listView(Model model, Pageable pageable) {
        Page<E> page = service.findAll(pageable);
        model.addAttribute("table", buildDataTable(page));
        model.addAttribute("pageTitle", getEntityName() + "s");
        return getListTemplate();
    }

    /**
     * Detail view. Call from subclass with @GetMapping("/{id}").
     */
    public String detailView(Long id, Model model) {
        E entity = service.getById(id);
        model.addAttribute("entity", entity);
        model.addAttribute("pageTitle", getEntityName());
        return getDetailTemplate();
    }

    // ==================== HTMX Mutation Endpoints ====================

    @PostMapping
    @ResponseBody
    public HxResponse createHx(@RequestBody E entity, HttpServletRequest request) {
        E created = service.create(entity);
        return HxResponse.builder()
                .swap(HxSwap.NONE)
                .trigger("cv:toast", Map.of(
                        "message", getEntityName() + " created successfully",
                        "type", "success"
                ))
                .redirect(getEntityPath() + "/" + created.getId())
                .build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public HxResponse updateHx(@PathVariable Long id, @RequestBody E entity, HttpServletRequest request) {
        entity.setId(id);
        service.update(entity);
        return HxResponse.builder()
                .swap(HxSwap.NONE)
                .trigger("cv:toast", Map.of(
                        "message", getEntityName() + " updated successfully",
                        "type", "success"
                ))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public HxResponse deleteHx(@PathVariable Long id, HttpServletRequest request) {
        service.delete(id);
        return HxResponse.builder()
                .swap(HxSwap.NONE)
                .trigger("cv:toast", Map.of(
                        "message", getEntityName() + " deleted successfully",
                        "type", "success"
                ))
                .redirect(getEntityPath())
                .build();
    }

    protected boolean isHxRequest(HttpServletRequest request) {
        return HxRequestFilter.isHxRequest(request);
    }
}
