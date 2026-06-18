package hu.clinvio.ui.business.controller;

import hu.clinvio.ui.components.basic.CvStatCard;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Abstract base controller for dashboard views.
 *
 * <p>Provides a standard pattern for dashboards with stat cards and recent items:</p>
 * <pre>{@code
 * @Controller
 * @RequestMapping("/dashboard")
 * public class DashboardController extends CvDashboardController {
 *
 *     @Override
 *     protected List<CvStatCard> getStatCards() {
 *         return List.of(
 *             CvStatCard.of("42", "Total Orders").variant(StatVariant.PRIMARY).icon("box"),
 *             CvStatCard.of("12", "Pending").variant(StatVariant.WARNING).icon("clock")
 *         );
 *     }
 *
 *     @Override
 *     protected List<Order> getRecentItems() {
 *         return orderService.findTop5Recent();
 *     }
 *
 *     @Override
 *     protected String getDashboardTemplate() {
 *         return "dashboard/main";
 *     }
 * }
 * }</pre>
 */
public abstract class CvDashboardController {

    /**
     * Get the stat cards to display on the dashboard.
     */
    protected abstract List<CvStatCard> getStatCards();

    /**
     * Get recent items to display on the dashboard.
     */
    protected abstract List<?> getRecentItems();

    /**
     * Get the dashboard template path.
     */
    protected abstract String getDashboardTemplate();

    /**
     * Get the dashboard page title.
     */
    protected String getDashboardTitle() {
        return "Dashboard";
    }

    /**
     * Dashboard view handler.
     */
    public String dashboard(Model model) {
        model.addAttribute("statCards", getStatCards());
        model.addAttribute("recentItems", getRecentItems());
        model.addAttribute("pageTitle", getDashboardTitle());
        return getDashboardTemplate();
    }
}
