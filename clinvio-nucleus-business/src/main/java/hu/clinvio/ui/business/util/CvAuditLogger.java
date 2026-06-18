package hu.clinvio.ui.business.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for audit logging of business operations.
 *
 * <p>Use in service methods to log important business actions:</p>
 * <pre>{@code
 * @Autowired
 * private CvAuditLogger auditLogger;
 *
 * @Transactional
 * public Order createOrder(Order order) {
 *     Order saved = super.create(order);
 *     auditLogger.logCreate("Order", saved.getId(), "Customer: " + order.getCustomerName());
 *     return saved;
 * }
 * }</pre>
 */
@Component
public class CvAuditLogger {

    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Log entity creation.
     */
    public void logCreate(String entityType, Object entityId, String details) {
        auditLog.info("CREATE {} id={} details='{}' at={}",
                entityType, entityId, details, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * Log entity update.
     */
    public void logUpdate(String entityType, Object entityId, String field, Object oldValue, Object newValue) {
        auditLog.info("UPDATE {} id={} field={} oldValue='{}' newValue='{}' at={}",
                entityType, entityId, field, oldValue, newValue, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * Log entity deletion.
     */
    public void logDelete(String entityType, Object entityId, String reason) {
        auditLog.info("DELETE {} id={} reason='{}' at={}",
                entityType, entityId, reason, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * Log status change.
     */
    public void logStatusChange(String entityType, Object entityId, String oldStatus, String newStatus) {
        auditLog.info("STATUS_CHANGE {} id={} from='{}' to='{}' at={}",
                entityType, entityId, oldStatus, newStatus, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * Log business action.
     */
    public void logAction(String action, String entityType, Object entityId, String details) {
        auditLog.info("ACTION {} {} id={} details='{}' at={}",
                action, entityType, entityId, details, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * Log security event.
     */
    public void logSecurity(String event, String details) {
        auditLog.warn("SECURITY event='{}' details='{}' at={}",
                event, details, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * Log data access.
     */
    public void logAccess(String entityType, Object entityId, String operation) {
        auditLog.info("ACCESS {} id={} operation='{}' at={}",
                entityType, entityId, operation, LocalDateTime.now().format(FORMATTER));
    }
}
