package hu.clinvio.ui.components.form;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.Map;

/**
 * Clinvio Text Field component.
 * Maps to Bootstrap 5 form-control with Clinvio theme styling.
 */
public class CvTextField extends AbstractCvComponent {

    private String label;
    private String value;
    private String placeholder;
    private String type;
    private boolean required;
    private boolean disabled;
    private boolean readonly;
    private String helpText;
    private FieldState state;
    private String errorMessage;
    private String hxPost;
    private String hxGet;
    private String hxTarget;
    private String hxTrigger;

    public CvTextField() {
        super();
        this.type = "text";
        this.state = FieldState.DEFAULT;
    }

    public CvTextField(String label) {
        super();
        this.label = label;
        this.type = "text";
        this.state = FieldState.DEFAULT;
    }

    public static CvTextField text(String label) {
        CvTextField field = new CvTextField(label);
        field.setType("text");
        return field;
    }

    public static CvTextField email(String label) {
        CvTextField field = new CvTextField(label);
        field.setType("email");
        return field;
    }

    public static CvTextField password(String label) {
        CvTextField field = new CvTextField(label);
        field.setType("password");
        return field;
    }

    public static CvTextField number(String label) {
        CvTextField field = new CvTextField(label);
        field.setType("number");
        return field;
    }

    public static CvTextField date(String label) {
        CvTextField field = new CvTextField(label);
        field.setType("date");
        return field;
    }

    public CvTextField value(String value) {
        this.value = value;
        return this;
    }

    public CvTextField placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public CvTextField required(boolean required) {
        this.required = required;
        return this;
    }

    public CvTextField disabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public CvTextField helpText(String helpText) {
        this.helpText = helpText;
        return this;
    }

    public CvTextField state(FieldState state) {
        this.state = state;
        return this;
    }

    public CvTextField errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.state = FieldState.ERROR;
        return this;
    }

    // Getters and Setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String placeholder) { this.placeholder = placeholder; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public boolean isDisabled() { return disabled; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }

    public boolean isReadonly() { return readonly; }
    public void setReadonly(boolean readonly) { this.readonly = readonly; }

    public String getHelpText() { return helpText; }
    public void setHelpText(String helpText) { this.helpText = helpText; }

    public FieldState getState() { return state; }
    public void setState(FieldState state) { this.state = state; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        if (errorMessage != null) this.state = FieldState.ERROR;
    }

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
        return CvComponentType.TEXT_FIELD.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.TEXT_FIELD.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("label", label);
        model.put("value", value);
        model.put("placeholder", placeholder);
        model.put("type", type);
        model.put("required", required);
        model.put("disabled", disabled);
        model.put("readonly", readonly);
        model.put("helpText", helpText);
        model.put("state", state);
        model.put("errorMessage", errorMessage);
        model.put("hxPost", hxPost);
        model.put("hxGet", hxGet);
        model.put("hxTarget", hxTarget);
        model.put("hxTrigger", hxTrigger);
        return model;
    }

    public enum FieldState {
        DEFAULT(""),
        SUCCESS("cv-field-success"),
        ERROR("cv-field-error");

        private final String cssClass;

        FieldState(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }
}
