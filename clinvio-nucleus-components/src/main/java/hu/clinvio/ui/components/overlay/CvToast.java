package hu.clinvio.ui.components.overlay;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

public class CvToast extends AbstractCvComponent {

    private String title;
    private String message;
    private ToastSeverity severity = ToastSeverity.INFO;
    private ToastPosition position = ToastPosition.TOP_RIGHT;
    private boolean dismissible = true;
    private boolean autoDismiss = true;
    private int duration = 5000;
    private String icon;

    public CvToast() {
        super();
    }

    public static CvToast of(String title, String message) {
        CvToast toast = new CvToast();
        toast.title = title;
        toast.message = message;
        return toast;
    }

    public CvToast severity(ToastSeverity severity) { this.severity = severity; return this; }
    public CvToast position(ToastPosition position) { this.position = position; return this; }
    public CvToast dismissible(boolean dismissible) { this.dismissible = dismissible; return this; }
    public CvToast autoDismiss(boolean autoDismiss) { this.autoDismiss = autoDismiss; return this; }
    public CvToast duration(int duration) { this.duration = duration; return this; }
    public CvToast icon(String icon) { this.icon = icon; return this; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public ToastSeverity getSeverity() { return severity; }
    public void setSeverity(ToastSeverity severity) { this.severity = severity; }
    public ToastPosition getPosition() { return position; }
    public void setPosition(ToastPosition position) { this.position = position; }
    public boolean isDismissible() { return dismissible; }
    public void setDismissible(boolean dismissible) { this.dismissible = dismissible; }
    public boolean isAutoDismiss() { return autoDismiss; }
    public void setAutoDismiss(boolean autoDismiss) { this.autoDismiss = autoDismiss; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getIcon() { return icon != null ? icon : severity.getDefaultIcon(); }
    public void setIcon(String icon) { this.icon = icon; }

    @Override
    public String getComponentType() {
        return CvComponentType.TOAST.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.TOAST.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("title", title);
        model.put("message", message);
        model.put("severity", severity);
        model.put("position", position.getCssClass());
        model.put("dismissible", dismissible);
        model.put("autoDismiss", autoDismiss);
        model.put("duration", duration);
        model.put("icon", getIcon());
        return model;
    }

    public enum ToastSeverity {
        INFO("cv-toast-info", "bi-info-circle"),
        SUCCESS("cv-toast-success", "bi-check-circle"),
        WARNING("cv-toast-warning", "bi-exclamation-triangle"),
        ERROR("cv-toast-error", "bi-x-circle");

        private final String cssClass;
        private final String defaultIcon;

        ToastSeverity(String cssClass, String defaultIcon) {
            this.cssClass = cssClass;
            this.defaultIcon = defaultIcon;
        }

        public String getCssClass() { return cssClass; }
        public String getDefaultIcon() { return defaultIcon; }
    }

    public enum ToastPosition {
        TOP_RIGHT("toast-top-right"),
        TOP_LEFT("toast-top-left"),
        BOTTOM_RIGHT("toast-bottom-right"),
        BOTTOM_LEFT("toast-bottom-left");

        private final String cssClass;

        ToastPosition(String cssClass) { this.cssClass = cssClass; }

        public String getCssClass() { return cssClass; }
    }
}
