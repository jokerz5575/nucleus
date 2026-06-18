package hu.clinvio.ui.components.form;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

public class CvToggle extends AbstractCvComponent {

    private String label;
    private boolean on;
    private boolean disabled;
    private ToggleVariant variant = ToggleVariant.PRIMARY;
    private LabelPosition labelPosition = LabelPosition.RIGHT;
    private String hxPost;
    private String hxGet;
    private String hxTarget;
    private String hxTrigger;

    public CvToggle() {
        super();
    }

    public CvToggle(String label) {
        super();
        this.label = label;
    }

    public static CvToggle of(String label) {
        return new CvToggle(label);
    }

    public CvToggle on(boolean on) { this.on = on; return this; }
    public CvToggle disabled(boolean disabled) { this.disabled = disabled; return this; }
    public CvToggle variant(ToggleVariant variant) { this.variant = variant; return this; }
    public CvToggle labelPosition(LabelPosition labelPosition) { this.labelPosition = labelPosition; return this; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public boolean isOn() { return on; }
    public void setOn(boolean on) { this.on = on; }
    public boolean isDisabled() { return disabled; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }
    public ToggleVariant getVariant() { return variant; }
    public void setVariant(ToggleVariant variant) { this.variant = variant; }
    public LabelPosition getLabelPosition() { return labelPosition; }
    public void setLabelPosition(LabelPosition labelPosition) { this.labelPosition = labelPosition; }

    public String getHxPost() { return hxPost; }
    public void setHxPost(String hxPost) { this.hxPost = hxPost; }
    public String getHxGet() { return hxGet; }
    public void setHxGet(String hxGet) { this.hxGet = hxGet; }
    public String getHxTarget() { return hxTarget; }
    public void setHxTarget(String hxTarget) { this.hxTarget = hxTarget; }
    public String getHxTrigger() { return hxTrigger; }
    public void setHxTrigger(String hxTrigger) { this.hxTrigger = hxTrigger; }

    @Override
    public String getComponentType() {
        return CvComponentType.TOGGLE.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.TOGGLE.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("label", label);
        model.put("on", on);
        model.put("disabled", disabled);
        model.put("variant", variant);
        model.put("labelPosition", labelPosition);
        model.put("hxPost", hxPost);
        model.put("hxGet", hxGet);
        model.put("hxTarget", hxTarget);
        model.put("hxTrigger", hxTrigger);
        return model;
    }

    public enum ToggleVariant {
        PRIMARY("cv-toggle-primary"),
        SUCCESS("cv-toggle-success"),
        DANGER("cv-toggle-danger");

        private final String cssClass;

        ToggleVariant(String cssClass) { this.cssClass = cssClass; }

        public String getCssClass() { return cssClass; }
    }

    public enum LabelPosition {
        LEFT, RIGHT
    }
}
