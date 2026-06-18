package hu.clinvio.ui.components.basic;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

/**
 * Clinvio Button component.
 * Maps to Bootstrap 5 buttons with Clinvio theme styling.
 */
public class CvButton extends AbstractCvComponent {

    private String label;
    private ButtonType type;
    private ButtonSize size;
    private String icon;
    private boolean disabled;
    private boolean loading;
    private String hxGet;
    private String hxPost;
    private String hxTarget;
    private String hxSwap;

    public CvButton() {
        super();
        this.type = ButtonType.PRIMARY;
        this.size = ButtonSize.DEFAULT;
    }

    public CvButton(String label) {
        super();
        this.label = label;
        this.type = ButtonType.PRIMARY;
        this.size = ButtonSize.DEFAULT;
    }

    public static CvButton primary(String label) {
        CvButton button = new CvButton(label);
        button.setType(ButtonType.PRIMARY);
        return button;
    }

    public static CvButton secondary(String label) {
        CvButton button = new CvButton(label);
        button.setType(ButtonType.SECONDARY);
        return button;
    }

    public static CvButton success(String label) {
        CvButton button = new CvButton(label);
        button.setType(ButtonType.SUCCESS);
        return button;
    }

    public static CvButton danger(String label) {
        CvButton button = new CvButton(label);
        button.setType(ButtonType.DANGER);
        return button;
    }

    public static CvButton accent(String label) {
        CvButton button = new CvButton(label);
        button.setType(ButtonType.ACCENT);
        return button;
    }

    public static CvButton outline(String label) {
        CvButton button = new CvButton(label);
        button.setType(ButtonType.OUTLINE_PRIMARY);
        return button;
    }

    public static CvButton ghost(String label) {
        CvButton button = new CvButton(label);
        button.setType(ButtonType.GHOST);
        return button;
    }

    public CvButton icon(String icon) {
        this.icon = icon;
        return this;
    }

    public CvButton size(ButtonSize size) {
        this.size = size;
        return this;
    }

    public CvButton disabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public CvButton loading(boolean loading) {
        this.loading = loading;
        return this;
    }

    public CvButton hxGet(String url) {
        this.hxGet = url;
        return this;
    }

    public CvButton hxPost(String url) {
        this.hxPost = url;
        return this;
    }

    public CvButton hxTarget(String target) {
        this.hxTarget = target;
        return this;
    }

    public CvButton hxSwap(String swap) {
        this.hxSwap = swap;
        return this;
    }

    // Getters and Setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public ButtonType getType() { return type; }
    public void setType(ButtonType type) { this.type = type; }

    public ButtonSize getSize() { return size; }
    public void setSize(ButtonSize size) { this.size = size; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public boolean isDisabled() { return disabled; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }

    public boolean isLoading() { return loading; }
    public void setLoading(boolean loading) { this.loading = loading; }

    public String getHxGet() { return hxGet; }
    public void setHxGet(String hxGet) { this.hxGet = hxGet; }

    public String getHxPost() { return hxPost; }
    public void setHxPost(String hxPost) { this.hxPost = hxPost; }

    public String getHxTarget() { return hxTarget; }
    public void setHxTarget(String hxTarget) { this.hxTarget = hxTarget; }

    public String getHxSwap() { return hxSwap; }
    public void setHxSwap(String hxSwap) { this.hxSwap = hxSwap; }

    @Override
    public String getComponentType() {
        return CvComponentType.BUTTON.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.BUTTON.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("label", label);
        model.put("type", type);
        model.put("size", size);
        model.put("icon", icon);
        model.put("disabled", disabled);
        model.put("loading", loading);
        model.put("hxGet", hxGet);
        model.put("hxPost", hxPost);
        model.put("hxTarget", hxTarget);
        model.put("hxSwap", hxSwap);
        return model;
    }

    public enum ButtonType {
        PRIMARY("btn-primary"),
        SECONDARY("btn-secondary"),
        SUCCESS("btn-success"),
        DANGER("btn-danger"),
        ACCENT("btn-accent"),
        OUTLINE_PRIMARY("btn-outline-primary"),
        OUTLINE_SECONDARY("btn-outline-secondary"),
        SOFT_PRIMARY("btn-soft-primary"),
        GHOST("btn-ghost");

        private final String cssClass;

        ButtonType(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

    public enum ButtonSize {
        SMALL("btn-sm"),
        DEFAULT(""),
        LARGE("btn-lg");

        private final String cssClass;

        ButtonSize(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }
}
