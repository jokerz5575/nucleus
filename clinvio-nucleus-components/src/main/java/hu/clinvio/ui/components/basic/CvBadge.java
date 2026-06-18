package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

public class CvBadge extends AbstractCvComponent {

    private String text;
    private BadgeVariant variant = BadgeVariant.PRIMARY;

    public CvBadge() {
        super();
    }

    public CvBadge(String text) {
        super();
        this.text = text;
    }

    public static CvBadge of(String text) {
        return new CvBadge(text);
    }

    public CvBadge text(String text) { this.text = text; return this; }
    public CvBadge variant(BadgeVariant variant) { this.variant = variant; return this; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public BadgeVariant getVariant() { return variant; }
    public void setVariant(BadgeVariant variant) { this.variant = variant; }

    @Override
    public String getComponentType() {
        return CvComponentType.BADGE.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.BADGE.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("text", text);
        model.put("variant", variant.name().toLowerCase());
        return model;
    }

    public enum BadgeVariant {
        PRIMARY, SUCCESS, WARNING, DANGER, INFO, NEUTRAL
    }

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
