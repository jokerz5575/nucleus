package hu.clinvio.ui.components.form;

import hu.clinvio.ui.core.component.AbstractCvComponent;
import hu.clinvio.ui.core.component.CvComponentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clinvio Select component.
 * Maps to Bootstrap 5 form-select with Clinvio theme styling.
 */
public class CvSelect extends AbstractCvComponent {

    private String label;
    private String value;
    private List<Option> options = new ArrayList<>();
    private String placeholder;
    private boolean required;
    private boolean disabled;
    private String helpText;
    private CvTextField.FieldState state;
    private String errorMessage;
    private String hxPost;
    private String hxGet;
    private String hxTarget;

    public CvSelect() {
        super();
        this.state = CvTextField.FieldState.DEFAULT;
    }

    public CvSelect(String label) {
        super();
        this.label = label;
        this.state = CvTextField.FieldState.DEFAULT;
    }

    public CvSelect addOption(String value, String label) {
        this.options.add(new Option(value, label));
        return this;
    }

    public CvSelect options(List<Option> options) {
        this.options = options;
        return this;
    }

    // Getters and Setters
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }

    public String getPlaceholder() { return placeholder; }
    public void setPlaceholder(String placeholder) { this.placeholder = placeholder; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public boolean isDisabled() { return disabled; }
    public void setDisabled(boolean disabled) { this.disabled = disabled; }

    public String getHelpText() { return helpText; }
    public void setHelpText(String helpText) { this.helpText = helpText; }

    public CvTextField.FieldState getState() { return state; }
    public void setState(CvTextField.FieldState state) { this.state = state; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getHxPost() { return hxPost; }
    public void setHxPost(String hxPost) { this.hxPost = hxPost; }

    public String getHxGet() { return hxGet; }
    public void setHxGet(String hxGet) { this.hxGet = hxGet; }

    public String getHxTarget() { return hxTarget; }
    public void setHxTarget(String hxTarget) { this.hxTarget = hxTarget; }

    @Override
    public String getComponentType() {
        return CvComponentType.SELECT.getType();
    }

    @Override
    public String getTemplate() {
        return CvComponentType.SELECT.getDefaultTemplate();
    }

    @Override
    public Map<String, Object> getTemplateModel() {
        Map<String, Object> model = super.getTemplateModel();
        model.put("label", label);
        model.put("value", value);
        model.put("options", options);
        model.put("placeholder", placeholder);
        model.put("required", required);
        model.put("disabled", disabled);
        model.put("helpText", helpText);
        model.put("state", state);
        model.put("errorMessage", errorMessage);
        model.put("hxPost", hxPost);
        model.put("hxGet", hxGet);
        model.put("hxTarget", hxTarget);
        return model;
    }

    public static class Option {
        private final String value;
        private final String label;
        private boolean selected;

        public Option(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public Option(String value, String label, boolean selected) {
            this.value = value;
            this.label = label;
            this.selected = selected;
        }

        public String getValue() { return value; }
        public String getLabel() { return label; }
        public boolean isSelected() { return selected; }
        public void setSelected(boolean selected) { this.selected = selected; }
    }
}
