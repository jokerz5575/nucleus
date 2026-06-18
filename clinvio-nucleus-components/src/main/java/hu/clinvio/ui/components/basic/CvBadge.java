package hu.clinvio.ui.components.basic;

/**
 * Utility class for creating badge HTML strings.
 * Badges are simple enough to not need a full component - just generate HTML.
 *
 * <pre>{@code
 * String badge = CvBadge.success("Active");
 * String badge = CvBadge.danger("Blocked");
 * String badge = CvBadge.fromStatus(order.getStatus().getLabel(), order.getStatus().getBadgeClass());
 * }</pre>
 */
public class CvBadge {

    private CvBadge() {}

    public static String primary(String text) {
        return "<span class=\"cv-badge cv-badge-primary\">" + escapeHtml(text) + "</span>";
    }

    public static String success(String text) {
        return "<span class=\"cv-badge cv-badge-success\">" + escapeHtml(text) + "</span>";
    }

    public static String warning(String text) {
        return "<span class=\"cv-badge cv-badge-warning\">" + escapeHtml(text) + "</span>";
    }

    public static String danger(String text) {
        return "<span class=\"cv-badge cv-badge-danger\">" + escapeHtml(text) + "</span>";
    }

    public static String info(String text) {
        return "<span class=\"cv-badge cv-badge-info\">" + escapeHtml(text) + "</span>";
    }

    public static String neutral(String text) {
        return "<span class=\"cv-badge cv-badge-neutral\">" + escapeHtml(text) + "</span>";
    }

    public static String fromStatus(String text, String badgeClass) {
        return "<span class=\"cv-badge " + escapeHtml(badgeClass) + "\">" + escapeHtml(text) + "</span>";
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
