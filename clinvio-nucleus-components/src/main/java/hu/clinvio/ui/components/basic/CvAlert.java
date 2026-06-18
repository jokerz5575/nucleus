package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

public class CvAlert extends AbstractCvComponent {

    private String message;
    private AlertType type = AlertType.INFO;
    private boolean dismissible = true;
    private String icon;

    public static CvAlert of(String message, AlertType type) {
        CvAlert alert = new CvAlert();
        alert.message = message;
        alert.type = type;
        return alert;
    }

    public CvAlert dismissible(boolean dismissible) { this.dismissible = dismissible; return this; }
    public CvAlert icon(String icon) { this.icon = icon; return this; }

    @Override
    public String getComponentType() { return CvComponentType.ALERT.getType(); }

    @Override
    public String getTemplate() {         return CvComponentType.ALERT.getDefaultTemplate(); }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("message", message);
        model.put("type", type);
        model.put("dismissible", dismissible);
        model.put("icon", icon != null ? icon : type.getDefaultIcon());
        return model;
    }

    public String getMessage() { return message; }
    public AlertType getType() { return type; }
    public boolean isDismissible() { return dismissible; }
    public String getIcon() { return icon; }

    public enum AlertType {
        INFO("cv-alert-info", "bi-info-circle"),
        SUCCESS("cv-alert-success", "bi-check-circle"),
        WARNING("cv-alert-warning", "bi-exclamation-triangle"),
        DANGER("cv-alert-danger", "bi-x-circle");

        private final String cssClass;
        private final String defaultIcon;

        AlertType(String cssClass, String defaultIcon) {
            this.cssClass = cssClass;
            this.defaultIcon = defaultIcon;
        }

        public String getCssClass() { return cssClass; }
        public String getDefaultIcon() { return defaultIcon; }
    }
}
